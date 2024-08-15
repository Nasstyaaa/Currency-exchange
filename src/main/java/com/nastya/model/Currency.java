package com.nastya.model;

public class Currency {
    private int id;
    private String name;
    private String code;
    private String sign;


    public Currency() {
    }

    public Currency(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.name = fullName;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
