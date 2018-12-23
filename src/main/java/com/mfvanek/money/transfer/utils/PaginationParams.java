package com.mfvanek.money.transfer.utils;

import com.mfvanek.money.transfer.interfaces.repositories.Pagination;
import lombok.ToString;
import spark.Request;

@ToString
public final class PaginationParams implements Pagination {

    private final int recordsPerPage;
    private final int pageNumber;
    private final boolean valid;

    private PaginationParams(Request request) {
        int limit = 0;
        int page = 0;
        boolean valid = false;
        final String limitStr = request.queryParams("limit");
        if (limitStr != null) {
            limit = Integer.valueOf(limitStr, 10);
            final String pageStr = request.queryParams("page");
            if (pageStr != null) {
                page = Integer.valueOf(pageStr, 10);
            } else {
                page = 1; // default
            }
            valid = true;
        }

        this.recordsPerPage = limit;
        this.pageNumber = page;
        this.valid = valid;
    }

    @Override
    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    public static PaginationParams from(Request request) {
        final PaginationParams pgParams = new PaginationParams(request);
        if (pgParams.isNotValid()) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
        return pgParams;
    }
}
