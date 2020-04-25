package me.stevemmmmm.configapi.core;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import com.avaje.ebean.validation.NotNull;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

public class DataManager {
    public static void main(String[] args) {
        double x = readData("playerranks", "PlayerRanks", Double.class, getDefaultSQLAction(SQLAction.READ));
        System.out.println(x);
    }

    /**
     * Reads data from a database. Readable databases and their tables are:<br><br>
     *
     * - playerranks > PlayerRanks
     *
     * @param database The database to connect to (PebbleHost prefix)
     * @param table The table that will be read
     * @param type The class the result will cast to
     * @param <T> The Generic return
     * @return The requested data
     */
    @SuppressWarnings("unchecked")
    public static <T> T readData(String database, String table, Class<T> type, ResultSetAction action) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://na02-sql.pebblehost.com:3306/customer_112484_playerranks", "customer_112484_playerranks", "uHDEl#RCS1gdd!CS1lA8");

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM customer_112484_" + database + "." + table;
            ResultSet resultSet = statement.executeQuery(query);

            String typeName = type.getName();

            String root = null;

            if (type.getName().length() >= 10) {
                root = typeName.substring(0, 10);
            } else if (type.isPrimitive()) {
                root = "java.lang.";
                typeName = root + typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
            }

            if (root != null) {
                if (root.equalsIgnoreCase("java.lang.") && !typeName.equalsIgnoreCase("java.lang.String")) {
                    Class<?> reflectedClass = Class.forName(typeName);

                    String parseSuffix = typeName.split("\\.")[2];

                    if (parseSuffix.equalsIgnoreCase("Integer")) {
                        parseSuffix = "Int";
                    }

                    Method parseMethod = reflectedClass.getMethod("parse" + parseSuffix, String.class);

                    Object result = parseMethod.invoke(null, String.valueOf(action.run(resultSet)));

                    if (type.isPrimitive()) {
                        return (T) result;
                    }

                    return type.cast(result);
                }
            }

            return type.cast(action.run(resultSet));
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return type.cast(null);
    }

    public static @NotNull ResultSetAction getDefaultSQLAction(SQLAction action) {
        if (action == SQLAction.READ) {
            return resultSet -> 69;
        }

        if (action == SQLAction.DISPLAY) {
            return resultSet -> {
                try {
                    while (resultSet.next()) {
                        System.out.println(resultSet.getString("rank"));
                    }

                    return "";
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }

                return null;
            };
        }

        return resultSet -> { throw new IllegalArgumentException("Invalid argument!"); };
    }
}
