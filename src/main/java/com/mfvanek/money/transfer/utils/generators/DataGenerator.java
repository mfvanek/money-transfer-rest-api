package com.mfvanek.money.transfer.utils.generators;

import com.mfvanek.money.transfer.utils.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DataGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataGenerator.class);

    private final Context context;
    private int partiesCount = 100_000;
    private int accountsPerClient = 10;
    private boolean initialTransactions = true;
    private boolean runImmediately = true;
    private int clientTransactionsCount = 1_000_000;

    private DataGenerator(Context context) {
        this.context = context;
    }

    public DataGenerator withPartiesCount(final int count) {
        partiesCount = count;
        return this;
    }

    public DataGenerator withAccountsPerClient(final int count) {
        this.accountsPerClient = count;
        return this;
    }

    public DataGenerator withoutInitialTransactions() {
        this.initialTransactions = false;
        return this;
    }

    public DataGenerator withoutRunningTransactions() {
        this.runImmediately = false;
        return this;
    }

    public DataGenerator withClientTransactions(final int count) {
        // TODO validate count. could be zero!!! getInstance unit test
        this.clientTransactionsCount = count;
        if (count > 0) {
            initialTransactions = true;
        }
        return this;
    }

    public DataGenerator withoutClientTransactions() {
        this.clientTransactionsCount = 0;
        return this;
    }

    public void generate() {
        try {
            final List<Long> partyIds = generateParties();
            final List<Long> accountIds = generateAccounts(partyIds);
            if (initialTransactions) {
                generateInitialTransactions(accountIds);
                if (clientTransactionsCount > 0) {
                    generateClientTransactions(accountIds);
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private List<Long> generateParties() {
        final AbstractGenerator partyGenerator = new RandomPartyGenerator(context, partiesCount);
        final List<Long> partyIds = partyGenerator.generate();
        // Our bank already exists
        logger.debug("Party ids count = {}", partyIds.size());
        logger.debug("Party repository size = {}", context.getPartyRepository().size());
        return partyIds;
    }

    private List<Long> generateAccounts(final List<Long> partyIds) {
        final AbstractGenerator accountGenerator = new RandomAccountGenerator(context, partyIds, accountsPerClient);
        final List<Long> accountIds = accountGenerator.generate();
        // Our bank account already exists
        logger.debug("Account ids count = {}", accountIds.size());
        logger.debug("Account repository size = {}", context.getAccountsRepository().size());
        return accountIds;
    }

    private void generateInitialTransactions(final List<Long> accountIds) {
        final AbstractGenerator initialTransactionGenerator = new InitialTransactionGenerator(context, accountIds, runImmediately);
        final List<Long> initialTrnIds = initialTransactionGenerator.generate();
        logger.debug("Initial transaction ids count = {}", initialTrnIds.size());
        logger.debug("Transaction repository size = {}", context.getTransactionRepository().size());
        context.getAccountsRepository().validateBalance();
    }

    private void generateClientTransactions(final List<Long> accountIds) {
        final AbstractGenerator transactionGenerator = new RandomTransactionGenerator(
                context, accountIds, runImmediately, 10, clientTransactionsCount);
        final List<Long> trnIds = transactionGenerator.generate();
        logger.debug("Transaction ids count = {}", trnIds.size());
        logger.debug("Transaction repository size = {}", context.getTransactionRepository().size());
        context.getAccountsRepository().validateBalance();
    }

    public static DataGenerator getInstance(Context context) {
        return new DataGenerator(context);
    }
}
