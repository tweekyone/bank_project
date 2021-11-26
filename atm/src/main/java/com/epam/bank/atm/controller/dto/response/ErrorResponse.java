package com.epam.bank.atm.controller.dto.response;

public class ErrorResponse {
    private final String type;
    private final short status;

    public ErrorResponse(String type, short status) {
        this.type = type;
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public short getStatus() {
        return this.status;
    }
}
