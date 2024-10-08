package com.nastya.dao;

import com.nastya.exception.DBErrorException;
import com.nastya.exception.DuplicateCurrencyPairException;
import com.nastya.exception.ExchangeRateNotFoundException;
import com.nastya.exception.InvalidCurrencyPairException;
import com.nastya.model.ExchangeRate;
import com.nastya.util.BuilderUtil;
import com.nastya.util.DataSourceUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

import com.nastya.model.Currency;

import java.util.List;

public class ExchangeRateDAO {

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> rates = new ArrayList<>();

        try (Connection connection = DataSourceUtil.getConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("""
                    SELECT exchange_rates.id AS rate_id, exchange_rates.rate,
                    b_c.id AS base_id, b_c.code AS base_code,b_c.full_name AS base_name, b_c.sign AS base_sign,
                    t_c.id AS target_id, t_c.code AS target_code, t_c.full_name AS target_name, t_c.sign AS target_sign
                    FROM exchange_rates
                    JOIN currencies AS b_c ON exchange_rates.base_currency_id = b_c.id
                    JOIN currencies AS t_c ON exchange_rates.target_currency_id = t_c.id""");

            while (resultSet.next()) {
                rates.add(BuilderUtil.createExchangeRate(resultSet));
            }
            return rates;

        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }


    public ExchangeRate find(String baseCode, String targetCode){
        try(Connection connection = DataSourceUtil.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT exchange_rates.id AS rate_id, exchange_rates.rate,
                    b_c.id AS base_id, b_c.code AS base_code, b_c.full_name AS base_name, b_c.sign AS base_sign,
                    t_c.id AS target_id, t_c.code AS target_code, t_c.full_name AS target_name, t_c.sign AS target_sign
                    FROM exchange_rates
                    JOIN currencies AS b_c ON exchange_rates.base_currency_id = b_c.id
                    JOIN currencies AS t_c ON exchange_rates.target_currency_id = t_c.id
                    WHERE b_c.code = ? AND t_c.code = ?""");

            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                throw new ExchangeRateNotFoundException();
            }
            return BuilderUtil.createExchangeRate(resultSet);

        }catch (SQLException exception){
            throw new DBErrorException();
        }
    }


    public ExchangeRate update(ExchangeRate exchangeRate, BigDecimal rate){
        try (Connection connection = DataSourceUtil.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    UPDATE exchange_rates SET rate=? WHERE id=?""");
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, exchangeRate.getId());
            try {
                preparedStatement.executeUpdate();
            }catch (SQLException exception){
                throw new ExchangeRateNotFoundException();
            }
            exchangeRate.setRate(rate);
            return exchangeRate;

        }catch (SQLException exception){
            throw new DBErrorException();
        }
    }


    public ExchangeRate save(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        try (Connection connection = DataSourceUtil.getConnection()) {
            PreparedStatement prStatement = connection.prepareStatement("""
                        INSERT INTO exchange_rates(base_currency_id, target_currency_id, rate) VALUES (?, ?, ?);
                        """);
            prStatement.setInt(1, baseCurrency.getId());
            prStatement.setInt(2, targetCurrency.getId());
            prStatement.setBigDecimal(3, rate);

            try {
                prStatement.executeUpdate();
            }catch (SQLException exception){
                throw new DuplicateCurrencyPairException();
            }

            ResultSet generatedKeys = prStatement.getGeneratedKeys();
            return new ExchangeRate(generatedKeys.getInt(1), baseCurrency, targetCurrency, rate);

        } catch (SQLException exception) {
            throw new DBErrorException();
        }
    }
}
