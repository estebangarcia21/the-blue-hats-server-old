package me.stevemmmmm.configapi.sqlmanagement.managers;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.MySQLConnection;

public interface MySQLDatabase {
    String getName();

    String getConnectionUrl();

    String getUsername();

    String getPassword();
}
