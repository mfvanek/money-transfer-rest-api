/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces;

import io.github.mfvanek.money.transfer.enums.TransactionState;

import java.math.BigDecimal;

public interface Transaction extends Identifiable {

    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();

    TransactionState getState();

    boolean run();
}
