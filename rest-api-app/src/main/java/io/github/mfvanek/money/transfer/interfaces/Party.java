/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer.interfaces;

import io.github.mfvanek.money.transfer.enums.PartyType;

public interface Party extends Identifiable {

    String getName();

    boolean isPrivatePerson();

    boolean isLegalPerson();

    PartyType getPartyType();

    String getTaxIdentificationNumber();
}
