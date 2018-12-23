package com.mfvanek.money.transfer.interfaces;

import com.mfvanek.money.transfer.enums.PartyType;

public interface Party extends Identifiable {

    String getName();

    boolean isPrivatePerson();

    boolean isLegalPerson();

    PartyType getPartyType();

    String getTaxIdentificationNumber();
}
