package com.mfvanek.money.transfer.interfaces.repositories;

public interface Pageable<T> {

    PagedResult<T> getAll(int pageNumber, int recordsPerPage);

    default PagedResult<T> getAll(Pagination pagination) {
        return getAll(pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
