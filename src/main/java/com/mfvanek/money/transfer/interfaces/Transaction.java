package com.mfvanek.money.transfer.interfaces;

import com.mfvanek.money.transfer.enums.TransactionState;

import java.math.BigDecimal;

public interface Transaction extends Identifiable {

    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();

    TransactionState getState();

    boolean run();
}
