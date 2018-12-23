package com.mfvanek.money.transfer.utils;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.utils.generators.DataGenerator;
import lombok.Getter;

import java.math.BigDecimal;

public final class Bank {

    @Getter
    private final Context context;

    private Bank() {
        this.context = Context.create();
    }

    public void generateData() {
        DataGenerator.getInstance(context)
                .withPartiesCount(100)
                .withAccountsPerClient(2)
                .withClientTransactions(10_000)
                .generate();
    }

    public boolean transfer(Account debit, Account credit, BigDecimal amount) {
        // TODO
        return false;
    }

    private static class LazyHolder {
        static final Bank INSTANCE = new Bank();
    }

    public static Bank getInstance() {
        return LazyHolder.INSTANCE;
    }
}
