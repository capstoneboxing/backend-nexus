//package com.nexus.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.OffsetDateTime;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/public")
//@Tag(name = "Health", description = "Health check and system status endpoints")
//public class HealthController {
//
//    @Operation(
//            summary = "Health check",
//            description = "Returns the current status of the API. Used for monitoring and uptime checks. This endpoint is public."
//    )
//    @GetMapping("/health")
//    public Map<String, Object> health() {
//        return Map.of(
//                "status", "UP",
//                "service", "Perfect Boxer API",
//                "timestamp", OffsetDateTime.now(),
//                "version", "v1"
//        );
//    }
//}
