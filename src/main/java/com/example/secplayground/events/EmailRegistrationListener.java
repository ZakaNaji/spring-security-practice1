package com.example.secplayground.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class EmailRegistrationListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCustomerRegistration(CustomerRegistredEvent event) {
        log.warn("Verification token for {}: {}", event.getUsername(), event.getToken().getToken());
    }
}
