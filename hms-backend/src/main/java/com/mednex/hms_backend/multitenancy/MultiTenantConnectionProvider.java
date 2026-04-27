package com.mednex.hms_backend.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {

    // SECURITY: only allow these schema names — never trust raw user input!
    private static final List<String> ALLOWED_SCHEMAS =
            Arrays.asList("hospital_a", "hospital_b", "public_shared");

    @Autowired
    private DataSource dataSource;

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource; // used during startup
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource; // same datasource — schema changes via SET
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        // Validate against whitelist — NEVER skip this step
        if (!ALLOWED_SCHEMAS.contains(tenantIdentifier)) {
            throw new IllegalArgumentException(
                    "Unknown tenant: " + tenantIdentifier);
        }

        Connection conn = dataSource.getConnection();

        // THIS IS THE MAGIC LINE ↓
        // SET search_path tells PostgreSQL: "look in THIS schema for tables"
        conn.createStatement()
                .execute("SET search_path TO " + tenantIdentifier);

        return conn;
    }
}
