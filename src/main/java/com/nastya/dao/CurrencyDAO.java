package com.nastya.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.nastya.exception.CurrencyCodeExistsException;
import com.nastya.exception.CurrencyNotFoundException;
import com.nastya.exception.DBErrorException;
import com.nastya.model.Currency;
import com.nastya.util.DataSourceUtil;
import com.nastya.util.CurrencyBuilderUtil;

public class CurrencyDAO {
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = DataSourceUtil.get().getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM currencies");

            while (resultSet.next()) {
                currencies.add(CurrencyBuilderUtil.create(resultSet));
            }
            return currencies;

        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }


    public Currency find(String code) {
        try (Connection connection = DataSourceUtil.get().getConnection()) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM currencies WHERE code=?");

            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new CurrencyNotFoundException();
            }
            return CurrencyBuilderUtil.create(resultSet);

        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }


    public Currency save(Currency currency) {
        try (Connection connection = DataSourceUtil.get().getConnection()) {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)");
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            currency.setId(generatedKeys.getInt(1));

            return currency;

        } catch (SQLException exception) {
            if (exception.getErrorCode() == 19) {
                throw new CurrencyCodeExistsException();
            }
            throw new DBErrorException();
        }
    }
}
