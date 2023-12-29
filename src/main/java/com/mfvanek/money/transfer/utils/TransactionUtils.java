/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@UtilityClass
public final class TransactionUtils {

    public static Pair<Long, Long> getRandomAccountIds(final List<Long> accountIds) {
        final int debitIdx = getRandom().nextInt(accountIds.size());
        final int creditIdx = debitIdx != 0 ? debitIdx - 1 : debitIdx + 1;
        final Pair<Long, Long> result = Pair.of(accountIds.get(debitIdx), accountIds.get(creditIdx));
        log.trace("Generated accounts pair = {}", result);
        return result;
    }

    /**
     * Gets Random instance in multithread environment
     * @return Random instance
     */
    private static Random getRandom() {
        return ThreadLocalRandom.current();
    }

    public static BigDecimal generateAmount(int min, int max) {
        final BigDecimal amount = BigDecimal.valueOf(min + getRandom().nextInt(max - min), 2);
        log.trace("Generated amount = {}", amount);
        return amount;
    }
}
