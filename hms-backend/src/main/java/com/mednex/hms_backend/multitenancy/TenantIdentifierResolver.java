package com.mednex.hms_backend.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

        @Override
        public String resolveCurrentTenantIdentifier () {
        String tenant = TenantContext.getCurrentTenant();

        // If no tenant set yet, use the shared schema
        return (tenant != null && !tenant.isBlank())
                ? tenant
                : "public_shared";
    }

        @Override
        public boolean validateExistingCurrentSessions () {
        return true;

    }
}
