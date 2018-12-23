package com.mfvanek.money.transfer.models.accounts;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.models.currencies.BaseCurrency;
import com.mfvanek.money.transfer.models.parties.AbstractParty;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidAccountTest {

    @Test
    void invalidAccount() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);

        assertFalse(a.isValid());
        assertTrue(a.isNotValid());
        assertEquals(Long.valueOf(-1), a.getId());

        assertEquals(AbstractAccount.getInvalid(), a);
        assertEquals(-1, a.hashCode());

        assertEquals("", a.getNumber());
        assertEquals(BigDecimal.valueOf(0), a.getBalance());
        assertEquals(BaseCurrency.getInvalid(), a.getCurrency());
        assertEquals(AbstractParty.getInvalid(), a.getHolder());
        assertFalse(a.isActive());
    }

    @Test
    void toStringImpl() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);
        assertTrue(a.toString().startsWith("InvalidAccount{"));
    }
}