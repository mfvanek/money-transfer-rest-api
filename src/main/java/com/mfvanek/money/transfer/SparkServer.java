/*
 * Copyright (c) 2018. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer;

import com.mfvanek.money.transfer.interfaces.Account;
import com.mfvanek.money.transfer.interfaces.Identifiable;
import com.mfvanek.money.transfer.interfaces.Party;
import com.mfvanek.money.transfer.interfaces.Transaction;
import com.mfvanek.money.transfer.interfaces.repositories.AccountsRepository;
import com.mfvanek.money.transfer.interfaces.repositories.Repository;
import com.mfvanek.money.transfer.interfaces.repositories.TransactionRepository;
import com.mfvanek.money.transfer.utils.Bank;
import com.mfvanek.money.transfer.utils.JsonUtils;
import com.mfvanek.money.transfer.utils.PaginationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

final class SparkServer {

    private static final String WITHOUT_DATA = "do_not_generate_data";
    private static final Logger logger = LoggerFactory.getLogger(SparkServer.class);

    static void start() {
        final String[] args = {WITHOUT_DATA};
        SparkServer.main(args);
        Spark.awaitInitialization();
    }

    static void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {
        generateData(args);

        // TODO API versioning
        // TODO POST\PUT should return object that they've created or changed
        Spark.port(9999);
        Spark.threadPool(10);
        Spark.after((req, res) -> res.type("application/json"));

        initPartyRoutes();
        initAccountRoutes();
        initTransactionRoutes();
        initExceptionsHandling();
    }

    private static void initPartyRoutes() {
        // http://localhost:9999/parties?limit=10
        // http://localhost:9999/parties?page=2&limit=20
        Spark.get("/parties", (req, res) -> {
            final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/parties/1
        Spark.get("/parties/:id", (req, res) -> {
            final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
            return JsonUtils.make().toJson(findById(Party.class, repository, req));
        });

        // http://localhost:9999/parties/1/accounts
        Spark.get("/parties/:id/accounts", (req, res) -> {
            final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
            final Party party = findById(Party.class, repository, req);
            final AccountsRepository accountsRepository = Bank.getInstance().getContext().getAccountsRepository();
            return JsonUtils.make().toJson(accountsRepository.getByHolder(party));
        });
    }

    private static void initAccountRoutes() {
        // http://localhost:9999/accounts?limit=10
        Spark.get("/accounts", (req, res) -> {
            final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/accounts/1
        Spark.get("/accounts/:id", (req, res) -> {
            final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
            return JsonUtils.make().toJson(findById(Account.class, repository, req));
        });

        // http://localhost:9999/accounts/1/transactions?limit=100
        Spark.get("/accounts/:id/transactions", (req, res) -> {
            final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
            final Account account = findById(Account.class, repository, req);
            final TransactionRepository transactionRepository = Bank.getInstance().getContext().getTransactionRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(transactionRepository.getByAccount(account, pgParams));
        });
    }

    private static void initTransactionRoutes() {
        // http://localhost:9999/transactions?limit=100
        Spark.get("/transactions", (req, res) -> {
            final Repository<Transaction> repository = Bank.getInstance().getContext().getTransactionRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/transactions/1
        Spark.get("/transactions/:id", (req, res) -> {
            final Repository<Transaction> repository = Bank.getInstance().getContext().getTransactionRepository();
            return JsonUtils.make().toJson(findById(Transaction.class, repository, req));
        });
    }

    private static void initExceptionsHandling() {
        Spark.exception(IllegalArgumentException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NullPointerException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NumberFormatException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        Spark.exception(NoSuchElementException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));
    }

    private static void generateData(String[] args) {
        if (args != null && args.length > 0 && WITHOUT_DATA.equals(args[0].toLowerCase())) {
            return;
        }

        try {
            Bank.getInstance().generateData();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private static void fillErrorInfo(Response res, Exception err, int errCode) {
        res.type("application/json");
        res.status(errCode);
        res.body(JsonUtils.toJson(err, errCode));
    }

    private static <T extends Identifiable> T findById(Class<T> type, Repository<T> repository, Request req) {
        final Long id = getId(req);
        final T t = repository.getById(id);
        if (t.isNotValid()) {
            throw new NoSuchElementException(String.format("%s with id %d is not found", type.getSimpleName(), id));
        }
        return t;
    }

    private static Long getId(Request req) {
        return Long.valueOf(req.params("id"), 10);
    }
}
