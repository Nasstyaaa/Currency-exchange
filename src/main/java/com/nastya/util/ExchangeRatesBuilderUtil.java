package com.nastya.util;

import com.nastya.model.Currency;
import com.nastya.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRatesBuilderUtil {
    public static ExchangeRate create(ResultSet resultSet) throws SQLException {
        ExchangeRate rate = new ExchangeRate();
        rate.setId(resultSet.getInt("rate_id"));

        rate.setBaseCurrency(new Currency(resultSet.getInt("base_id"),
                resultSet.getString("base_code"),
                resultSet.getString("base_name"),
                resultSet.getString("base_sign")));

        rate.setTargetCurrency(new Currency(resultSet.getInt("target_id"),
                resultSet.getString("target_code"),
                resultSet.getString("target_name"),
                resultSet.getString("target_sign")));

        rate.setRate(resultSet.getBigDecimal("rate"));

        return rate;
    }
}
