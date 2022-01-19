package com.epam.bank.operatorinterface.service;

import java.util.Random;
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class CardUtil {

    //creates card number close to real with checkSum - 4000 00xx xxxx xxxx, 16 digits
    public static String generateCardNumber() {
        Random random = new Random();
        int a = random.nextInt(1000);
        if (a / 100 == 0) {
            a += 100;
        }
        String bin = "400000" + a;
        String can = String.format("%06d", random.nextInt(1000000));
        String checkSum = null;
        try {
            checkSum = new LuhnCheckDigit().calculate(bin + can);
        } catch (CheckDigitException e) {
            e.printStackTrace();
        }
        if ((bin + can + checkSum).length() > 16) {
            generateCardNumber();
        }
        return bin + can + checkSum;
    }

    public static String generatePinCode() {
        int pin = Integer.parseInt(String.format("%04d", new Random().nextInt(10000)));
        if (pin / 1000 == 0) {
            pin = pin * 10;
        }
        return String.valueOf(pin);
    }
}
