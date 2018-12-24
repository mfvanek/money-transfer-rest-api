/*
 * Copyright (c) 2018. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer.models.parties;

import com.mfvanek.money.transfer.enums.PartyType;
import com.mfvanek.money.transfer.interfaces.Party;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegalPersonTest {

    @Test
    void attributes() {
        final Party party = AbstractParty.makeLegalPerson(11L, "1234567890", "test");
        assertNotNull(party);
        assertEquals(Long.valueOf(11L), party.getId());
        assertTrue(party.isValid());
        assertFalse(party.isNotValid());
        assertTrue(party.isLegalPerson());
        assertFalse(party.isPrivatePerson());
        assertEquals(PartyType.LEGAL_PERSON, party.getPartyType());
        assertEquals("test", party.getName());
        assertEquals("1234567890", party.getTaxIdentificationNumber());
    }
}