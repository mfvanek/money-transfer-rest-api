package com.mfvanek.money.transfer.repositories;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Party;
import com.mfvanek.money.transfer.interfaces.repositories.AccountsRepository;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import com.mfvanek.money.transfer.utils.BaseTest;
import com.mfvanek.money.transfer.utils.Context;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountsRepositoryTest extends BaseTest {

    @Test
    void getInitialBalance() {
        final AccountsRepository repository = Context.create().getAccountsRepository();
        assertEquals(BigDecimal.valueOf(100_000_000.00d), repository.getInitialBalance());
    }

    @Test
    void getOurBankMainAccount() {
        final AccountsRepository repository = Context.create().getAccountsRepository();
        final Account a = repository.getOurBankMainAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(repository.getInitialBalance(), a.getBalance());
    }

    @Test
    void size() {
        final AccountsRepository repository = Context.create().getAccountsRepository();
        assertEquals(1, repository.size());
        repository.addOurBankAccount("20202810100000012345", BigDecimal.ZERO);
        assertEquals(2, repository.size());
    }

    @Test
    void getByHolder() {
        final Context context = Context.create();
        final AccountsRepository repository = context.getAccountsRepository();
        final PartyRepository partyRepository = context.getPartyRepository();
        final Party party = partyRepository.addLegalPerson("1234567890", "test");

        Collection<Account> accounts = repository.getByHolder(party);
        assertNotNull(accounts);
        assertEquals(0, accounts.size());

        final Account clientAccount = repository.addPassiveAccount("40702810001234567890", party);
        accounts = repository.getByHolder(party);
        assertEquals(1, accounts.size());
        assertEquals(clientAccount, accounts.iterator().next());

        accounts = repository.getByHolder(partyRepository.getOurBank());
        assertEquals(1, accounts.size());
        assertEquals(repository.getOurBankMainAccount(), accounts.iterator().next());
    }
}