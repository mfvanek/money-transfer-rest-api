/*
 * Copyright (c) 2018. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
                assertEquals(200, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
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
                assertEquals(200, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
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
                assertEquals(400, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
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
                assertEquals(400, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Invalid pagination parameters\"\n" +
                        "}", json);
            }
        }
    }
}