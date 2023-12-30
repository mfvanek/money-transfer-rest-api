package io.github.mfvanek.money.transfer.repositories;

import io.github.mfvanek.money.transfer.interfaces.Transaction;
import io.github.mfvanek.money.transfer.interfaces.repositories.PagedResult;
import io.github.mfvanek.money.transfer.interfaces.repositories.Pagination;
import io.github.mfvanek.money.transfer.interfaces.repositories.TransactionRepository;
import io.github.mfvanek.money.transfer.models.accounts.RussianAccount;
import io.github.mfvanek.money.transfer.utils.PaginationParams;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.Request;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultTransactionRepositoryTest {

    @Test
    void getByAccountShouldWork() {
        final TransactionRepository repository = new DefaultTransactionRepository();
        final Request request = Mockito.mock(Request.class);
        Mockito.when(request.queryParams("limit")).thenReturn("100");
        Mockito.when(request.queryParams("page")).thenReturn("1");
        final Pagination pagination = PaginationParams.from(request);
        final PagedResult<Transaction> result = repository.getByAccount(RussianAccount.getInvalid(), pagination);
        assertThat(result.hasMore()).isFalse();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageNumber()).isEqualTo(1);
        assertThat(result.getRecordsPerPage()).isEqualTo(100);
    }
}
