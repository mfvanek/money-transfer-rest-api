/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.repositories;

import com.mfvanek.money.transfer.interfaces.repositories.AccountsRepository;
import com.mfvanek.money.transfer.interfaces.repositories.Cleanable;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import com.mfvanek.money.transfer.interfaces.repositories.TransactionRepository;
import lombok.Getter;

@Getter
public class Context implements Cleanable {

    private final PartyRepository partyRepository;
    private final AccountsRepository accountsRepository;
    private final TransactionRepository transactionRepository;

    private Context(PartyRepository partyRepository,
                   AccountsRepository accountsRepository,
                   TransactionRepository transactionRepository) {
        this.partyRepository = partyRepository;
        this.accountsRepository = accountsRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void clear() {
        partyRepository.clear();
        accountsRepository.clear();
        transactionRepository.clear();
    }

    public static Context create() {
        final PartyRepository partyRepository = new DefaultPartyRepository();
        final AccountsRepository accountsRepository = new DefaultAccountsRepository(partyRepository);
        final TransactionRepository transactionRepository = new DefaultTransactionRepository();
        return new Context(partyRepository, accountsRepository, transactionRepository);
    }
}
