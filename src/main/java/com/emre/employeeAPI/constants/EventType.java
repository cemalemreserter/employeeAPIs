package com.emre.employeeAPI.constants;

public enum EventType {
    CREATE_EMPLOYEE("Create-Employee-Event"),
    DELETE_EMPLOYEE("Delete-Employee-Event"),
    UPDATE_EMPLOYEE("Update-Employee-Event");

    private String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
