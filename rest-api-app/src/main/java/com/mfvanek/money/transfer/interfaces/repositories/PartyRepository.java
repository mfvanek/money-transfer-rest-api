/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.interfaces.repositories;

import com.mfvanek.money.transfer.interfaces.Party;

public interface PartyRepository extends Repository<Party>, Cleanable {

    Party addLegalPerson(String taxIdentificationNumber, String name);

    Party addPrivatePerson(String taxIdentificationNumber, String firstName, String lastName);

    Party getOurBank();
}
