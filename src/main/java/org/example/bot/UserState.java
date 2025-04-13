package org.example.bot;

public enum UserState {
    IDLE(""),
    WAITING_FOR_AMOUNT(""),
    WAITING_FOR_DATE(""),
    WAITING_FOR_COMMENT("");

    private final String name;

    UserState(String name) {
        this.name = name;
    }
}