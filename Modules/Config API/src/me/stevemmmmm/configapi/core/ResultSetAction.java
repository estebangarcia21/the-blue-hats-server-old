package me.stevemmmmm.configapi.core;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import com.avaje.ebean.validation.NotNull;

import java.sql.ResultSet;

public interface ResultSetAction {
    Object run(ResultSet resultSet);
}
