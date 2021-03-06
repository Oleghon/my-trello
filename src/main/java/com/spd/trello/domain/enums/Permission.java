package com.spd.trello.domain.enums;

public enum Permission {
    READ("read"),
    WRITE("write"),
    UPDATE("update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
