package util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class TestRequestFactory {

    public static String getInternalByAccountRequestBody() {
        return getTransferRequestBody(RandomUtils.nextLong(), RandomStringUtils.randomNumeric(20),
            null, RandomUtils.nextDouble());
    }

    public static String getInternalByCardRequestBody() {
        return getTransferRequestBody(RandomUtils.nextLong(), null,
            RandomStringUtils.randomNumeric(16), RandomUtils.nextDouble());
    }

    private static String getTransferRequestBody(long sourceAccountId, String destinationAccountNumber,
                                                String destinationCardNumber, double amount) {
        return String.format("{"
                + "\"sourceAccountId\":\"%d\", "
                + "\"destinationAccountNumber\":%s, "
                + "\"destinationCardNumber\":%s, "
                + "\"amount\":\"%f\"}",
            sourceAccountId,
            prepareString(destinationAccountNumber),
            prepareString(destinationCardNumber),
            amount);
    }

    public static String getExternalRequestBody() {
        return String.format("{"
                + "\"sourceAccountId\":\"%d\", "
                + "\"destinationAccountNumber\":\"%s\", "
                + "\"amount\":\"%f\"}",
            RandomUtils.nextLong(), RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble());
    }

    private static String prepareString(String s) {
        return (s != null ? "\"" : "") + s + (s != null ? "\"" : "");
    }
}
