package com.epam.clientinterface.configuration.property;

import lombok.Value;

@Value
public class DatasourceConnectionProperties {
    String url;
    String username;
    String password;
}
