package com.mfvanek.money.transfer.interfaces.repositories;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Currency;
import com.mfvanek.money.transfer.interfaces.Party;

import java.math.BigDecimal;
import java.util.Collection;

public interface AccountsRepository extends Repository<Account> {

    Account addOurBankAccount(Currency currency, String number, BigDecimal balance);

    Account addOurBankAccount(String number, BigDecimal balance);

    Account getOurBankMainAccount();

    Account addPassiveAccount(Currency currency, String number, Party holder);

    Account addPassiveAccount(String number, Party holder);

    BigDecimal getInitialBalance();

    void validateBalance();

    Collection<Account> getByHolder(Party holder);
}
