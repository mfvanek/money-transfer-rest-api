/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.models.accounts;

import com.mfvanek.money.transfer.enums.Chapter;
import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import com.mfvanek.money.transfer.models.currencies.BaseCurrency;
import com.mfvanek.money.transfer.repositories.Context;
import com.mfvanek.money.transfer.utils.JsonUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RussianAccountTest {

    private final PartyRepository repository = Context.create().getPartyRepository();

    @Test
    void getChapter() {
        RussianAccount a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(Chapter.BALANCE, a.getChapter());
    }

    @Test
    void getId() {
        final Account a = makeAccount(11L);
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(Long.valueOf(11L), a.getId());
    }

    @Test
    void getNumber() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals("30102810100000000001", a.getNumber());
    }

    @Test
    void getCurrency() {
        Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BaseCurrency.getDefault(), a.getCurrency());

        a = RussianAccount.makePassiveBalance(1L, BaseCurrency.valueOf("USD"), "30102810100000000001", repository.getOurBank());
        assertNotNull(a);
        assertTrue(a.isValid());
        assertFalse(a.isActive());
        assertEquals(BaseCurrency.valueOf("USD"), a.getCurrency());
    }

    @Test
    void getBalance() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BigDecimal.valueOf(0L), a.getBalance());
    }

    @Test
    void getHolder() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(repository.getOurBank(), a.getHolder());
    }

    @Test
    void debitAndCredit() {
        final Account a = makeAccount();
        assertFalse(a.debit(BigDecimal.TEN));
        assertTrue(a.credit(BigDecimal.TEN));
        assertEquals(BigDecimal.TEN, a.getBalance());
        assertTrue(a.debit(BigDecimal.ONE));
        assertEquals(BigDecimal.valueOf(9), a.getBalance());
    }

    @Test
    void toStringImpl() {
        final Account a = makeAccount();
        assertTrue(a.toString().startsWith("RussianAccount{"));
        assertTrue(a.toString().contains(", chapter="));
        assertEquals("RussianAccount{id=1, currency=BaseCurrency(isoCode=RUB), number=30102810100000000001, active=true, balance=0, holder=Party{Revolut LLC, type=LEGAL_PERSON, tax identification number=7703408188, id=1}, chapter=BALANCE}",
                a.toString());
    }

    @Test
    void lockShouldBeTransient() {
        final Account a = makeAccount();
        final String json = JsonUtils.make().toJson(a);
        assertNotNull(json);
        assertFalse(json.contains("lock"));
        assertEquals("{\n" +
                "  \"chapter\": \"BALANCE\",\n" +
                "  \"id\": 1,\n" +
                "  \"currency\": {\n" +
                "    \"isoCode\": \"RUB\"\n" +
                "  },\n" +
                "  \"number\": \"30102810100000000001\",\n" +
                "  \"holder\": {\n" +
                "    \"name\": \"Revolut LLC\",\n" +
                "    \"id\": 1,\n" +
                "    \"partyType\": \"LEGAL_PERSON\",\n" +
                "    \"taxIdentificationNumber\": \"7703408188\"\n" +
                "  },\n" +
                "  \"active\": true,\n" +
                "  \"balance\": 0\n" +
                "}", json);
    }

    private RussianAccount makeAccount() {
        return makeAccount(1L);
    }

    private RussianAccount makeAccount(Long id) {
        return RussianAccount.makeActiveRouble(id, "30102810100000000001", repository.getOurBank());
    }
}