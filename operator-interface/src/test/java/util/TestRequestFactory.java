package util;

import com.epam.bank.operatorinterface.controller.dto.request.ExternalTransferRequest;
import com.epam.bank.operatorinterface.controller.dto.request.InternalTransferRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class TestRequestFactory {

    public static String getInternalByAccountRequestBody() throws JsonProcessingException {
        return getTransferRequestBody(RandomUtils.nextLong(), RandomStringUtils.randomNumeric(20),
            null, RandomUtils.nextDouble());
    }

    public static String getInternalByCardRequestBody() throws JsonProcessingException {
        return getTransferRequestBody(RandomUtils.nextLong(), null,
            RandomStringUtils.randomNumeric(16), RandomUtils.nextDouble());
    }

    private static String getTransferRequestBody(long sourceAccountId, String destinationAccountNumber,
                                                String destinationCardNumber, double amount)
        throws JsonProcessingException {

        InternalTransferRequest transferRequest = new InternalTransferRequest(
            sourceAccountId, destinationAccountNumber, destinationCardNumber, amount
        );

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(transferRequest);
    }

    public static String getExternalRequestBody() throws JsonProcessingException {

        ExternalTransferRequest transferRequest = new ExternalTransferRequest(
            RandomUtils.nextLong(), RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble()
        );

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(transferRequest);
    }
}
