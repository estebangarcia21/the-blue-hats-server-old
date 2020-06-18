package me.stevemmmmm.configapi.sqlmanagement.managers;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public interface MySQLDatabase {
    String getName();

    String getConnectionUrl();

    String getUsername();

    String getPassword();
}
