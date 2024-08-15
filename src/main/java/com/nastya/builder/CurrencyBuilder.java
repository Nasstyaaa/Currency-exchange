package com.nastya.builder;

import com.nastya.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyBuilder {
    public static Currency create(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();

        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }
}
