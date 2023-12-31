/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer;

import io.github.mfvanek.money.transfer.utils.JsonUtils;
import io.github.mfvanek.money.transfer.utils.TransactionPayload;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SparkServerTest {

    @BeforeAll
    static void setUp() {
        SparkServer.start();
    }

    @AfterAll
    static void tearDown() {
        SparkServer.stop();
    }

    @Test
    void getAllPartiesDefaultPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?limit=10");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 1,\n" +
                        "  \"recordsPerPage\": 10,\n" +
                        "  \"content\": [\n" +
                        "    {\n" +
                        "      \"name\": \"Revolut LLC\",\n" +
                        "      \"id\": 1,\n" +
                        "      \"partyType\": \"LEGAL_PERSON\",\n" +
                        "      \"taxIdentificationNumber\": \"7703408188\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllPartiesSecondPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?page=2&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 2,\n" +
                        "  \"recordsPerPage\": 20,\n" +
                        "  \"content\": []\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllPartiesInvalidPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?page=0&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Page number should be positive and starts with 1\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllPartiesWithoutPagination() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Invalid pagination parameters\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void transferMoneyWithTheSameAccount() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost("http://localhost:9999/transactions");
            final TransactionPayload payload = new TransactionPayload(1L, 1L, BigDecimal.valueOf(123456, 2));
            final String jsonPayload = JsonUtils.make().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Accounts must be different\"\n" +
                        "}", json);
            }
        }
    }
}
