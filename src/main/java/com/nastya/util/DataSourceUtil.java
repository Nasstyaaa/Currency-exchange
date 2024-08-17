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

    static File file = new File(DataSourceUtil.class.getClassLoader().getResource("currency_exchange.sqlite").getFile());
    private static final String url = "jdbc:sqlite:" + file.getAbsolutePath();
    private static final SQLiteDataSource ds = new SQLiteDataSource();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ds.setUrl(url);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
