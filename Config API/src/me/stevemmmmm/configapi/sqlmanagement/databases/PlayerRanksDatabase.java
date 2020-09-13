package me.stevemmmmm.configapi.sqlmanagement.databases;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.configapi.sqlmanagement.managers.MySQLDatabase;

public class PlayerRanksDatabase implements MySQLDatabase {
    @Override
    public String getName() {
        return "customer_112484_playerranks";
    }

    @Override
    public String getConnectionUrl() {
        return "jdbc:mysql://na02-sql.pebblehost.com:3306/customer_112484_playerranks";
    }

    @Override
    public String getUsername() {
        return "customer_112484_playerranks";
    }

    @Override
    public String getPassword() {
        return "uHDEl#RCS1gdd!CS1lA8";
    }
}
