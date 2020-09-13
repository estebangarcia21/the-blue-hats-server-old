package me.stevemmmmm.configapi.sqlmanagement.managers;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public enum SQLDefaultActions {
    READ(resultSet -> 69);

    private final ResultSetAction action;

    SQLDefaultActions(ResultSetAction action) {
        this.action = action;
    }

    public ResultSetAction getAction() {
        return action;
    }
}
