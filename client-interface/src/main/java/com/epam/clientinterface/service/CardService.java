package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.CardNotFoundException;
import com.epam.clientinterface.domain.exception.ChangePinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.service.util.NewPinValidator;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final EntityManagerFactory entityManagerFactory;

    public Card changePinCode(ChangePinRequest pinRequest) {
        Card card = cardRepository.findById(pinRequest.getCardId()).orElse(null);
        if (card == null) {
            throw new CardNotFoundException(pinRequest.getCardId());
        }

        NewPinValidator.validatePinCode(card, pinRequest);

        Integer pinCounter = card.getPinCounter();

        if (pinCounter == null) {
            card.setPinCounter(1);
            card.setPinCode(pinRequest.getNewPin());
            return cardRepository.save(card);
        } else if (pinCounter < 3) {
            card.setPinCounter(card.getPinCounter() + 1);
            card.setPinCode(pinRequest.getNewPin());
            return cardRepository.save(card);
        } else {
            throw new ChangePinException();
        }
    }

    @Scheduled(cron = "20 * * * * *")
    public void dropPinCounter() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        Transaction txn = session.beginTransaction();
        Query query = session.createQuery("update Card set pinCounter = :countParam");
        query.setParameter("countParam", 0);
        query.executeUpdate();
        txn.commit();
    }

    public @NonNull Card releaseCard(@NonNull Long accountId, @NonNull CardPlan plan) {

        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFoundException(accountId);
        }

        String pinCode = generatePinCode();
        String number;
        // TODO: a potentially infinite loop
        do {
            number = generateCardNumber();
        } while (cardRepository.findCardByNumber(number).isPresent());

        Card card = new Card(account.get(), number, pinCode, plan, LocalDateTime.now().plusYears(3), 0);
        return cardRepository.save(card);
    }

    protected String generateCardNumber() {
        return randomGenerateStringOfInt(16);
    }

    protected String generatePinCode() {
        return randomGenerateStringOfInt(4);
    }

    public String randomGenerateStringOfInt(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();
    }
}
