package com.kamash.ccb;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private static String DRIVER = "com.mysql.jdbc.Driver";
    private static String URL    = "jdbc:mysql://172.17.0.2/sakila";
    private static String USER   = "sakila";
    private static String PASS   = "sakila";
    private static ConnectionManager instance;

    public ConnectionManager() {
        try {
            Class.forName(DRIVER);
            logger.debug("Loaded DRIVER=" + DRIVER);
        }
        catch (Exception e) {
            logger.error("ConnectionManager ERROR: " + e);
        }
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            logger.debug("Making new ConnectionManager");
            instance = new ConnectionManager();
            logger.debug(" - ok");
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            logger.debug("Creating new Connection");
            return DriverManager.getConnection(URL, USER, PASS);
        }
        catch (Exception e) {
            logger.error("getConnection ERROR: " + e);
            throw new RuntimeException("getConnection ERROR: " + e);
        }
    }

}
