package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Calendar;

@Getter
@Setter
@AllArgsConstructor
public class Activity {

    private long id;
    private String userAgent;
    private String ip;

    private User user;
    private String requestMethod;
    private String url;

    private long transactionId;
    private Calendar sessionStart;
    private Calendar sessionEnd;
}
