package com.mfvanek.money.transfer.utils.generators;

import com.mfvanek.money.transfer.interfaces.Party;
import com.mfvanek.money.transfer.interfaces.repositories.PartyRepository;
import com.mfvanek.money.transfer.utils.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RandomPartyGenerator extends AbstractGenerator {

    private final int partiesCount;

    RandomPartyGenerator(final Context context, final int partiesCount) {
        super(context, "parties");
        // TODO validate partiesCount
        this.partiesCount = partiesCount;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(partiesCount);
        for (int i = 0; i < partiesCount; ++i) {
            futures.add(threadPool.submit(this::generateParty));
        }
        return futures;
    }

    private void generateParty() {
        final PartyRepository partyRepository = context.getPartyRepository();
        final int idx = counter.incrementAndGet();
        if (idx % 2 == 0) {
            Party pt = partyRepository.addLegalPerson(generateTaxId(idx, 10), generateCompanyName(idx));
            ids.add(pt.getId());
        } else {
            Party pt = partyRepository.addPrivatePerson(generateTaxId(idx, 12), generateFirstName(idx), "John");
            ids.add(pt.getId());
        }
    }

    private static String generateCompanyName(final int idx) {
        if (idx % 4 == 0) {
            return "Apple" + idx;
        } else {
            if (idx % 6 == 0) {
                return "Google" + idx;
            }
        }
        return "Amazon" + idx;
    }

    private static String generateFirstName(final int idx) {
        if (idx % 3 == 0) {
            return "Page" + idx;
        } else {
            if (idx % 5 == 0) {
                return "Bezos" + idx;
            } else {
                if (idx % 7 == 0) {
                    return "Gates" + idx;
                }
            }
        }
        return "Jobs" + idx;
    }

    private static String generateTaxId(final int idx, final int length) {
        return StringUtils.leftPad(String.valueOf(idx), length, '0');
    }
}
