package com.nastya.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.sqlite.SQLiteDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;


@WebListener
public class DataSourceUtil implements ServletContextListener {

    private static final String url = "jdbc:sqlite::resource:currency_exchange.sqlite";
    private static final SQLiteDataSource ds = new SQLiteDataSource();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ds.setUrl(url);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
