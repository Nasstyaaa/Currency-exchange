package com.nastya.util;

import com.nastya.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyBuilderUtil {
    public static Currency create(ResultSet resultSet) throws SQLException {
        Currency currency = new Currency();

        currency.setId(resultSet.getInt("id"));
        currency.setCode(resultSet.getString("code"));
        currency.setFullName(resultSet.getString("full_name"));
        currency.setSign(resultSet.getString("sign"));
        return currency;
    }
}
