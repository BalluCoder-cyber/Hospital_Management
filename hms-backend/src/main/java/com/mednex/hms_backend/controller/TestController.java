package com.mednex.hms_backend.controller;
import com.mednex.hms_backend.multitenancy.TenantContext;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/tenant")
    public Map<String, String> getCurrentTenant() {
        return Map.of(
                "currentTenant", TenantContext.getCurrentTenant(),
                "status", "Multi-tenancy is working!"
        );
    }
}

