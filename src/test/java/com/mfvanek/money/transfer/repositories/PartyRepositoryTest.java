package com.mfvanek.money.transfer.repositories;

import com.mfvanek.money.transfer.interfaces.Party;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyRepositoryTest {

    @Test
    void getById() {
        final PartyRepository repository = make();
        Party pt = repository.getById(1L);
        assertNotNull(pt);
        assertTrue(pt.isValid());

        pt = repository.getById(Long.MAX_VALUE);
        assertNotNull(pt);
        assertFalse(pt.isValid());
        assertEquals(repository.getInvalid(), pt);
    }

    @Test
    void getOurBank() {
        final PartyRepository repository = make();
        final Party pt = repository.getOurBank();
        assertNotNull(pt);
        assertTrue(pt.isValid());
        assertTrue(pt.isLegalPerson());
        assertEquals(Long.valueOf(1), pt.getId());
        assertEquals("7703408188", pt.getTaxIdentificationNumber());
        assertEquals("Revolut LLC", pt.getName());
    }

    private PartyRepository make() {
        return new DefaultPartyRepository();
    }
}