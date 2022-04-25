package at.demo.model;

import java.util.stream.Stream;

/**
 * Enumeration of available audit log events.
 *
 */
public enum AuditLogEvent {
    // USER EVENTS
    USER_LOGIN("user_login"),
    USER_LOGOUT("user_logout"),
    CREATE_USER("create_user"),
    UPDATE_USER("update_user"),
    DELETE_USER("delete_user"),
    // SERVICE ORDER EVENTS
    CREATE_SERVICE_ORDER("create_service_order"),
    UPDATE_SERVICE_ORDER("update_service_order"),
    DELETE_SERVICE_ORDER("delete_service_order"),
    // PRODUCT EVENTS
    CREATE_PRODUCT("create_product"),
    UPDATE_PRODUCT("update_product"),
    DELETE_PRODUCT("delete_product");

    private String name;

    AuditLogEvent(String name){ this.name = name;}

    public String getName() {
        return name;
    }

    public static AuditLogEvent of(String name) {
        return Stream.of(AuditLogEvent.values())
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
