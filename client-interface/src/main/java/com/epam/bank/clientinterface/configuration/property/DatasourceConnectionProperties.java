package com.epam.bank.clientinterface.configuration.property;

import lombok.Value;

@Value
public class DatasourceConnectionProperties {
    String url;
    String username;
    String password;
}
