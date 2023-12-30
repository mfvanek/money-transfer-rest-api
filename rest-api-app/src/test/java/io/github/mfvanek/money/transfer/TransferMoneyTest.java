/*
 * Copyright (c) 2018-2022. Ivan Vakhrushev. All rights reserved.
 * https://github.com/mfvanek
 */

package io.github.mfvanek.money.transfer;

import io.github.mfvanek.money.transfer.utils.Bank;
import io.github.mfvanek.money.transfer.utils.JsonUtils;
import io.github.mfvanek.money.transfer.utils.TransactionPayload;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferMoneyTest {

    @BeforeAll
    static void setUp() {
        SparkServer.startWithData();
    }

    @AfterAll
    static void tearDown() {
        SparkServer.stop();
        Bank.getInstance().getContext().clear();
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
                assertEquals(HttpStatus.SC_CREATED, response.getCode());
                assertTrue(response.containsHeader("Location"));
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType());
                final String json = EntityUtils.toString(entity);
                assertTrue(json.contains("\"amount\": 1234.56"));
                assertTrue(json.contains("\"state\": \"COMPLETED\""));
            }
        }
    }
}
