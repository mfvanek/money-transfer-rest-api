package com.mfvanek.money.transfer;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    void getAllParties() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("http://localhost:9999/parties?limit=10");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    void getAllPartiesWithoutPagination() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("http://localhost:9999/parties");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(400, response.getStatusLine().getStatusCode());
            }
        } catch (Throwable e) {
            fail(e);
        }
    }
}