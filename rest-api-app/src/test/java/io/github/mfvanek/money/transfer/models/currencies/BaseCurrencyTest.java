/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.models.currencies;

import io.github.mfvanek.money.transfer.interfaces.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseCurrencyTest {

    @Test
    void valueOf() {
        final Currency rub = BaseCurrency.valueOf("RUB");
        assertNotNull(rub);
        assertTrue(rub.isValid());
        assertFalse(rub.isNotValid());
        assertEquals("RUB", rub.getISOCode());
        assertEquals(BaseCurrency.valueOf("RUB"), rub);
        assertSame(BaseCurrency.valueOf("RUB"), rub);

        final Currency usd = BaseCurrency.valueOf("USD");
        assertNotNull(usd);
        assertTrue(usd.isValid());
        assertFalse(usd.isNotValid());
        assertEquals("USD", usd.getISOCode());
        assertEquals(BaseCurrency.valueOf("USD"), usd);
        assertSame(BaseCurrency.valueOf("USD"), usd);

        assertNotEquals(usd, rub);
    }

    @Test
    void defaultCurrency() {
        final Currency def = BaseCurrency.getDefault();
        assertNotNull(def);
        assertTrue(def.isValid());
        assertEquals("RUB", def.getISOCode());
    }
}
