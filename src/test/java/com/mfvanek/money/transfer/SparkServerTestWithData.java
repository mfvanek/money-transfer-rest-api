/*
 * Copyright (c) 2018. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package com.mfvanek.money.transfer;

import com.mfvanek.money.transfer.utils.JsonUtils;
import com.mfvanek.money.transfer.utils.TransactionPayload;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SparkServerTestWithData {

    @BeforeAll
    static void setUp() {
        SparkServer.startWithData();
    }

    @AfterAll
    static void tearDown() {
        SparkServer.stop();
    }

    @Test
    void transferMoney() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost("http://localhost:9999/transactions");
            final TransactionPayload payload = new TransactionPayload(1L, 2L, BigDecimal.valueOf(123456, 2));
            final String jsonPayload = JsonUtils.make().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertTrue(json.contains("\"amount\": 1234.56"));
                assertTrue(json.contains("\"state\": \"COMPLETED\""));
            }
        }
    }
}