/*
 * Copyright (c) 2018. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.utils;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Transaction;
import com.mfvanek.money.transfer.interfaces.repositories.Repository;
import com.mfvanek.money.transfer.repositories.Context;
import com.mfvanek.money.transfer.utils.generators.DataGenerator;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

public final class Bank {

    @Getter
    private final Context context;

    private Bank(Context context) {
        this.context = context;
    }

    public void generateData() {
        DataGenerator.getInstance(context)
                .withPartiesCount(100)
                .withAccountsPerClient(2)
                .withClientTransactions(10_000)
                .generate();
    }

    public Transaction transfer(Long debitId, Long creditId, BigDecimal amount) {
        Objects.requireNonNull(debitId, "Payer account ID cannot be null");
        Objects.requireNonNull(creditId, "Recipient account ID cannot be null");
        Objects.requireNonNull(amount, "Amount of the transaction cannot be null");
        // TODO
        final Repository<Account> accountRepository = context.getAccountsRepository();

        throw new UnsupportedOperationException("");
    }

    private static class LazyHolder {
        static final Bank INSTANCE = new Bank(Context.create());
    }

    public static Bank getInstance() {
        return LazyHolder.INSTANCE;
    }
}
