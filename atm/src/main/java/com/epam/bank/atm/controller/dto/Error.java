package com.epam.bank.atm.controller.dto;

public class Error {
    private final String type;
    private final short status;
    private final String title;
    private final String detail;

    public Error(String type, short status, String title, String detail) {
        this.type = type;
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

    public String getType() {
        return this.type;
    }

    public short getStatus() {
        return this.status;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDetail() {
        return this.detail;
    }
}
