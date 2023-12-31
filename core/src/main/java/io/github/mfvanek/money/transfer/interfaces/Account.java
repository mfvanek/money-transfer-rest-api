/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

public interface Account extends Identifiable {

    String getNumber();

    Currency getCurrency();

    BigDecimal getBalance();

    boolean debit(BigDecimal amount);

    boolean credit(BigDecimal amount);

    Party getHolder();

    boolean isActive();

    Lock writeLock();
}
