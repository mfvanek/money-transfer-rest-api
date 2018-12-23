package com.mfvanek.money.transfer.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class TransactionUtils {

    private static final Logger logger = LoggerFactory.getLogger(TransactionUtils.class);

    private TransactionUtils() {}

    public static Pair<Long, Long> getRandomAccountIds(final List<Long> accountIds) {
        final int debitIdx = getRandom().nextInt(accountIds.size());
        final int creditIdx = debitIdx != 0 ? debitIdx - 1 : debitIdx + 1;
        final Pair<Long, Long> result = Pair.of(accountIds.get(debitIdx), accountIds.get(creditIdx));
        logger.trace("Generated accounts pair = {}", result);
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
        logger.trace("Generated amount = {}", amount);
        return amount;
    }
}
