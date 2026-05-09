package com.inventory.tracker.constants;

public class AppConstants {
    // Base API Paths
    public static final String API_V1_ALERTS = "/v1/alerts";
    public static final String API_V1_AUDIT_LOGS = "/v1/audit-logs";
    public static final String API_V1_AUTH = "/v1/auth";
    public static final String API_V1_IMPORT = "/v1/import";
    public static final String API_V1_INVENTORY = "/v1/inventory";
    public static final String API_V1_PRODUCTS = "/v1/products";
    public static final String API_V1_TRANSACTIONS = "/v1/transactions";
    public static final String API_V1_WAREHOUSES = "/v1/warehouses";

    // Kafka Topics
    public static final String INVENTORY_TOPIC = "inventory-update-topic";
    
    // Cache Constants
    public static final String CACHE_INVENTORY = "inventory";
    public static final String INVENTORY_CACHE_PREFIX = "inventory::";

    private AppConstants() {
        // Private constructor to hide the implicit public one
    }
}
