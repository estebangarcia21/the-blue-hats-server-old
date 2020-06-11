package me.stevemmmmm.configapi.sqlmanagement.managers;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class SQLManager {
    /**
     * Returns the <code>ResultSet</code> from the given query.
     *
     * @param database The database to connect to (PebbleHost prefix)
     * @param query The query to be executed
     * @return The requested data
     */
    public static ResultSet sendQuery(MySQLDatabase database, String query) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            query = String.format(query, database.getName());

            return statement.executeQuery(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> getMap(MySQLDatabase database, String table, Class<K> keyType, String keyColumn, Class<V> valueType, String valueColumn) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + database.getName() + "." + table);

            HashMap<K, V> result = new HashMap<>();

            while (resultSet.next()) {
                String keyEntry = resultSet.getString(keyColumn);
                String valueEntry = resultSet.getString(valueColumn);

                Object finalKey = keyEntry;
                Object finalValue = valueEntry;

                if (StringUtils.isNumeric(keyEntry)) {
                    finalKey = castToNumber(keyEntry, keyType);
                }

                if (StringUtils.isNumeric(valueEntry)) {
                    finalValue = castToNumber(valueEntry, valueType);
                }

                result.put(keyType.isPrimitive() ? (K) finalKey : keyType.cast(finalKey), valueType.isPrimitive() ? (V) finalValue : valueType.cast(finalValue));
            }

            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;

    }

    @SuppressWarnings("unchecked")
    public static <T> HashMap<UUID, T> getPlayerDataMap(MySQLDatabase database, String table, String uuidColumn, Class<T> valueType, String valueColumn, CastInstructions valueInstructions) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + database.getName() + "." + table);

            HashMap<UUID, T> result = new HashMap<>();

            while (resultSet.next()) {
                String keyEntry = resultSet.getString(uuidColumn);
                String valueEntry = resultSet.getString(valueColumn);

                Object finalValue = valueEntry;

                if (StringUtils.isNumeric(valueEntry)) {
                    finalValue = castToNumber(valueEntry, valueType);
                }

                if (valueInstructions != null) finalValue = valueInstructions.cast(keyEntry, valueEntry);

                result.put(UUID.fromString(keyEntry), valueType.isPrimitive() ? (T) finalValue : valueType.cast(finalValue));
            }

            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> HashMap<K, V> getComplexMap(MySQLDatabase database, String table, Class<K> keyType, String uuidColumn, Class<V> valueType, String valueColumn, CastInstructions keyInstructions, CastInstructions valueInstructions) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + database.getName() + "." + table);

            HashMap<K, V> result = new HashMap<>();

            while (resultSet.next()) {
                String keyEntry = resultSet.getString(uuidColumn);
                String valueEntry = resultSet.getString(valueColumn);

                Object finalKey = keyEntry;
                Object finalValue = valueEntry;

                if (StringUtils.isNumeric(keyEntry)) {
                    finalKey = castToNumber(keyEntry, keyType);
                }

                if (StringUtils.isNumeric(valueEntry)) {
                    finalValue = castToNumber(valueEntry, valueType);
                }

                if (keyInstructions != null) finalKey = keyInstructions.cast(keyEntry, valueEntry);
                if (valueInstructions != null) finalValue = valueInstructions.cast(keyEntry, valueEntry);

                result.put(keyType.isPrimitive() ? (K) finalKey : keyType.cast(finalKey), valueType.isPrimitive() ? (V) finalValue : valueType.cast(finalValue));
            }

            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Reads the <code>ResultSet</code> and casts the result to the specified class from the given query.
     *
     * @param database The database to connect to (PebbleHost prefix)
     * @param query The query to be executed
     * @param resultType The class that the data from the ResultSet will be casted to
     * @param action Action performed on the <code>ResultSet</code>
     * @return The requested data
     */
    @SuppressWarnings("unchecked")
    public static <T> T sendQuery(MySQLDatabase database, String query, Class<T> resultType, ResultSetAction action) {
        try {
            Connection connection = DriverManager.getConnection(database.getConnectionUrl(), database.getUsername(), database.getPassword());
            Statement statement = connection.createStatement();

            query = String.format(query, database.getName());
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
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }

        return resultType.cast(null);
    }

    @SuppressWarnings("unchecked")
    private static <T> T castToNumber(String value, Class<T> type) {
        try {
            String typeName = type.getName();

            String root = null;

            if (type.getName().length() >= 10) {
                root = typeName.substring(0, 10);
            } else if (type.isPrimitive()) {
                root = "java.lang.";

                if (typeName.equalsIgnoreCase("int")) {
                    typeName = "integer";
                }

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

                    Object parseResult = parseMethod.invoke(null, String.valueOf(value));

                    if (type.isPrimitive()) {
                        return (T) parseResult;
                    }

                    return type.cast(parseResult);
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
