package me.stevemmmmm.configapi.sqlmanagement.managers;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public interface ResultSetAction {
    Object run(ResultSet resultSet) throws SQLException;
}
