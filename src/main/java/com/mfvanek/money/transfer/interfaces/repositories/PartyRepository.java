package com.mfvanek.money.transfer.interfaces.repositories;

import com.mfvanek.money.transfer.interfaces.Party;

public interface PartyRepository extends Repository<Party> {

    Party addLegalPerson(String taxIdentificationNumber, String name);

    Party addPrivatePerson(String taxIdentificationNumber, String firstName, String lastName);

    Party getOurBank();
}
