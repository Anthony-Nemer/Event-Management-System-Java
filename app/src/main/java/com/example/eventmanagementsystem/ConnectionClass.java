package com.example.eventmanagementsystem;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    protected static String db = "ems_db";
    protected static String ip = "10.0.2.2";
    protected static String port = "3306";
    protected static String username = "root";
    protected static String password = "nemeranthony2004@";

    public Connection CONN() {
        Connection conn = null;
        Log.d("DBConnection", "Trying to connecttttttt");

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Define the connection string
            String connectionString = "jdbc:mysql://"+ip+":"+port+"/"+db+
                    "?connectTimeout=10000&socketTimeout=10000&autoReconnect=true&failOverReadOnly=false&maxReconnects=3&serverTimezone=UTC";

            Log.d("DBConnection", connectionString);
            conn = DriverManager.getConnection(connectionString, username, password);

            // Log success message if the connection is successful
            Log.d("DBConnection", "Successfully connected to the database");

        } catch (Exception e) {
            // Log error message in case of failure
            Log.d("DBConnection", "NOOOO to the database");
            Log.d("DBConnectionError", Objects.requireNonNull(e.getMessage()));
        }
        return conn;
    }
}
