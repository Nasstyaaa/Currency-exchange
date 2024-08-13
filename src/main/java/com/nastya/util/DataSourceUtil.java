package com.nastya.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;

@WebListener
public class DataSourceUtil implements ServletContextListener {

    private static final String url = "jdbc:sqlite:C:\\Users\\Anna\\IdeaProjects\\CurrencyExchange\\currency_exchange.sqlite";
    private static final SQLiteDataSource ds = new SQLiteDataSource();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ds.setUrl(url);
    }

    public static SQLiteDataSource get(){
        return ds;
    }
}
