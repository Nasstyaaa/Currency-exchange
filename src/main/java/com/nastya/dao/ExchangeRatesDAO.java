package com.nastya.dao;

import com.nastya.exception.DBErrorException;
import com.nastya.exception.DuplicateCurrencyPairException;
import com.nastya.exception.InvalidCurrencyPairException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.CurrencyBuilderUtil;
import com.nastya.util.DataSourceUtil;
import com.nastya.util.ExchangeRatesBuilderUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import com.nastya.model.Currency;

import java.util.List;

public class ExchangeRatesDAO {

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> rates = new ArrayList<>();

        try (Connection connection = DataSourceUtil.get().getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT exchange_rates.id AS rate_id, exchange_rates.rate," +
                    "b_c.id AS base_id, b_c.code AS base_code, " +
                    "b_c.full_name AS base_name, b_c.sign AS base_sign," +
                    "t_c.id AS target_id, t_c.code AS target_code, " +
                    "t_c.full_name AS target_name, t_c.sign AS target_sign " +
                    "FROM exchange_rates " +
                    "JOIN currencies AS b_c ON exchange_rates.base_currency_id = b_c.id " +
                    "JOIN currencies AS t_c ON exchange_rates.target_currency_id = t_c.id");

            while (resultSet.next()) {
                rates.add(ExchangeRatesBuilderUtil.create(resultSet));
            }
            return rates;

        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }





    public ExchangeRate save(String baseCode, String targetCode, double rate) {
        Currency targetCurrency;
        Currency baseCurrency;

        try (Connection connection = DataSourceUtil.get().getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM currencies WHERE code IN (?, ?);");
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new InvalidCurrencyPairException();
            }

            List<Currency> currencies = buildCurrencyPair(baseCode, resultSet);
            baseCurrency = currencies.get(0);
            targetCurrency = currencies.get(1);

            return insertValues(baseCurrency, targetCurrency, rate, connection);
        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }



    private ExchangeRate insertValues(Currency baseCurrency, Currency targetCurrency, double rate, Connection connection)
            throws SQLException {
        PreparedStatement prStatement = connection.prepareStatement("INSERT INTO " +
                "exchange_rates(base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)");
        prStatement.setInt(1, baseCurrency.getId());
        prStatement.setInt(2, targetCurrency.getId());
        prStatement.setBigDecimal(3, BigDecimal.valueOf(rate));
        try {
            prStatement.executeUpdate();
        }catch (SQLException exception){
            throw new DuplicateCurrencyPairException();
        }

        ResultSet generatedKeys = prStatement.getGeneratedKeys();
        return new ExchangeRate(generatedKeys.getInt(1), baseCurrency, targetCurrency, rate);
    }

    private List<Currency> buildCurrencyPair(String baseCode, ResultSet resultSet) throws SQLException {
        Currency base = CurrencyBuilderUtil.create(resultSet);
        if (!resultSet.next()){
            throw new InvalidCurrencyPairException();
        }
        Currency target = CurrencyBuilderUtil.create(resultSet);

        if(resultSet.getString("code").equals(baseCode)){
            return List.of(target, base);
        }else {
            return List.of(base, target);
        }
    }
}
