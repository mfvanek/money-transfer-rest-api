package com.mfvanek.money.transfer.repositories;

import com.mfvanek.money.transfer.interfaces.Party;
import com.mfvanek.money.transfer.interfaces.repositories.PagedResult;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import com.mfvanek.money.transfer.models.parties.AbstractParty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultPartyRepository implements PartyRepository {

    private final AtomicLong counter = new AtomicLong(0L);
    private final ConcurrentMap<Long, Party> parties = new ConcurrentHashMap<>();
    private final Long ourBankId;

    public DefaultPartyRepository() {
        final Party ourBank = addLegalPerson("7703408188", "Revolut LLC");
        ourBankId = ourBank.getId();
    }

    @Override
    public Party addLegalPerson(String taxIdentificationNumber, String name) {
        final Party legalPerson = AbstractParty.makeLegalPerson(counter.incrementAndGet(), taxIdentificationNumber, name);
        parties.putIfAbsent(legalPerson.getId(), legalPerson);
        return legalPerson;
    }

    @Override
    public Party addPrivatePerson(String taxIdentificationNumber, String firstName, String lastName) {
        final Party privatePerson = AbstractParty.makePrivatePerson(counter.incrementAndGet(), taxIdentificationNumber, firstName, lastName);
        parties.putIfAbsent(privatePerson.getId(), privatePerson);
        return privatePerson;
    }

    @Override
    public Party getById(Long id) {
        return parties.getOrDefault(id, getInvalid());
    }

    @Override
    public Party getOurBank() {
        // by design
        return getById(ourBankId);
    }

    @Override
    public PagedResult<Party> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultImpl.from(pageNumber, recordsPerPage, parties);
    }

    @Override
    public Party getInvalid() {
        return AbstractParty.getInvalid();
    }

    @Override
    public int size() {
        return parties.size();
    }
}
