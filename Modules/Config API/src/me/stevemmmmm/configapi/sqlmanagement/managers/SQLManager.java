package me.stevemmmmm.configapi.sqlmanagement.managers;

import me.stevemmmmm.configapi.sqlmanagement.databases.PlayerRanksDatabase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class SQLManager {
    public static void main(String[] args) {
        double x = sendQuery(PlayerRanksDatabase.class, "SELECT * FROM %s.PlayerRanks", Double.class, SQLDefaultActions.READ.getAction());
        System.out.println(x);
    }

    /**
     * Reads data from a database. Readable databases and their tables are:<br><br>
     *
     * - playerranks > PlayerRanks
     *
     * @param database The database to connect to (PebbleHost prefix)
     * @param resultType The class that the result will be cast to
     * @param <T> The return type of the query
     * @return The requested data
     */
    @SuppressWarnings("unchecked")
    public static <T> T sendQuery(Class<? extends MySQLDatabase> database, String query, Class<T> resultType, ResultSetAction action) {
        try {
            if (database == MySQLDatabase.class) {
                throw new IllegalArgumentException("You can not pass MySQLDatabase as a database!");
            }

            MySQLDatabase databaseInstance = database.newInstance();

            Connection connection = DriverManager.getConnection(databaseInstance.getConnectionUrl(), databaseInstance.getUsername(), databaseInstance.getPassword());

            Statement statement = connection.createStatement();

            query = String.format(query, databaseInstance.getName());
            ResultSet resultSet = statement.executeQuery(query);

            String typeName = resultType.getName();

            String root = null;

            if (resultType.getName().length() >= 10) {
                root = typeName.substring(0, 10);
            } else if (resultType.isPrimitive()) {
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

                    if (resultType.isPrimitive()) {
                        return (T) result;
                    }

                    return resultType.cast(result);
                }
            }

            return resultType.cast(action.run(resultSet));
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }

        return resultType.cast(null);
    }
}
