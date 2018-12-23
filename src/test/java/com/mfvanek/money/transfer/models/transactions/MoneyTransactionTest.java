package com.mfvanek.money.transfer.models.transactions;

import com.mfvanek.money.transfer.interfaces.repositories.AccountsRepository;
import com.mfvanek.money.transfer.utils.BaseTest;
import com.mfvanek.money.transfer.utils.Context;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTransactionTest extends BaseTest {

    @Test
    void constructorWithNulls() {
        final AccountsRepository repository = Context.create().getAccountsRepository();

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(null, null, null, null));
        assertEquals("Id cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, null, null, null));
        assertEquals("Debit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), null, null));
        assertEquals("Credit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), null));
        assertEquals("Amount cannot be null", e.getLocalizedMessage());
    }

    @Test
    void constructorWithInvalidValues() {
        final AccountsRepository repository = Context.create().getAccountsRepository();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.valueOf(-1)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.valueOf(0)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.ONE));
        assertEquals("Debit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getOurBankMainAccount(), repository.getInvalid(), BigDecimal.ONE));
        assertEquals("Credit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getOurBankMainAccount(), repository.getOurBankMainAccount(), BigDecimal.ONE));
        assertEquals("Accounts must be different", e.getLocalizedMessage());
    }
}