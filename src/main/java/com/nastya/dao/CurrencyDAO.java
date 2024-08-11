package com.nastya.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.nastya.models.Сurrency;
import com.nastya.dto.CurrencyDTO;

public class CurrencyDAO {
    private static final String URL = "jdbc:sqlite:C:\\Users\\Anna\\IdeaProjects\\CurrencyExchange\\currency-exchange.sqlite";

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


    public List<Сurrency> readAll() throws SQLException {
        List<Сurrency> currencies = new ArrayList<>();
        Statement statement = connection.createStatement();
        String SQL = "SELECT * FROM currencies";
        ResultSet resultSet = statement.executeQuery(SQL);

        while (resultSet.next()) {
            Сurrency currency = new Сurrency();

            currency.setId(resultSet.getInt("id"));
            currency.setCode(resultSet.getString("code"));
            currency.setFullName(resultSet.getString("full_name"));
            currency.setSign(resultSet.getString("sign"));

            currencies.add(currency);
        }

        return currencies;
    }


    public Сurrency read(String code) {
        Сurrency currency = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM currencies WHERE code=?");

            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            currency = new Сurrency();
            currency.setId(resultSet.getInt("id"));
            currency.setCode(resultSet.getString("code"));
            currency.setFullName(resultSet.getString("full_name"));
            currency.setSign(resultSet.getString("sign"));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return currency;
    }


    public Сurrency create(CurrencyDTO сurrency) throws SQLException {
        Сurrency addedCurrency = null;
        PreparedStatement preparedStatement =
                connection.prepareStatement("INSERT INTO currencies(code, full_name, sign) VALUES (?, ?, ?)");
        preparedStatement.setString(1, сurrency.getCode());
        preparedStatement.setString(2, сurrency.getFullName());
        preparedStatement.setString(3, сurrency.getSign());

        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        addedCurrency = new Сurrency(generatedKeys.getInt(1),
                сurrency.getCode(), сurrency.getFullName(), сurrency.getSign());
        return addedCurrency;
    }

    public boolean isExistCode(String code) throws SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("SELECT * FROM currencies WHERE code=? LIMIT 1");

        preparedStatement.setString(1, code);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
