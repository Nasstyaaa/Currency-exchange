package com.nastya.util;

import com.nastya.model.Currency;
import com.nastya.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BuilderUtil {

    public static Currency createCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();

        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }


    public static ExchangeRate createExchangeRate(ResultSet resultSet) throws SQLException {
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
