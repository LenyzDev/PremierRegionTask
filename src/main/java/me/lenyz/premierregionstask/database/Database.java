package me.lenyz.premierregionstask.database;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {
    public static Connection connection;
    public static void connect(Logger logger, FileConfiguration config) {
        String address = config.getString("MySQL.ip");
        String database = config.getString("MySQL.database");
        String username = config.getString("MySQL.username");
        String password = config.getString("MySQL.password");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+address+"/"+database+"?autoReconnect=true&useUnicode=yes", username, password);
            logger.info("has connected in MySql Database.");

        }catch (Exception e){
            e.printStackTrace();
            logger.severe("don't connect in MySql database.");
        }
    }

    public static void disconnect(Logger logger){
        if(connection != null){
            try {
                logger.info("has disconnected from MySql Database.");
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
                logger.severe("don't disconnect from MySql database.");
            }
        }
    }

    public static void reloadConnection(Logger logger, FileConfiguration config){
        disconnect(logger);
        connect(logger, config);
    }
}
