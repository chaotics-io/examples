package io.chaotics.examples.catfacts;

import io.chaotics.api.ApiResponse;
import io.chaotics.api.ApiSecretPair;
import io.chaotics.api.CreateApiRequest;
import io.chaotics.client.ChaoticsClient;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatFactServiceTest {

    private static final ChaoticsClient CHAOTICS_CLIENT = new ChaoticsClient();
    private static final List<ApiSecretPair> APIS_TO_DELETE = new ArrayList<>();
    private static final String CHAOTICS_URL = "https://api.chaotics.io/endpoint?apiId=";

    @AfterClass
    public static void afterTests() {
        APIS_TO_DELETE.forEach(apiSecretPair -> {
            try {
                CHAOTICS_CLIENT.deleteEndpoint(apiSecretPair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testSuccessfulResponse() throws IOException {
        ApiSecretPair apiSecretPair = CHAOTICS_CLIENT.createEndpoint(successfulResponse());
        APIS_TO_DELETE.add(apiSecretPair);
        ReliableCatFactService catFactService = new ReliableCatFactService(CHAOTICS_URL + apiSecretPair.getApiId());

        CatFact catFact = catFactService.getCatFact();

        Assert.assertEquals("Thank to an extremely efficient pair of kidneys, cats can hydrate themselves by drinking salt water.", catFact.getText());
    }

    @Test
    public void testDefaultFactIsGivenWhenServiceUnavailable() throws IOException {
        ApiSecretPair apiSecretPair = CHAOTICS_CLIENT.createEndpoint(unavailableResponse());
        APIS_TO_DELETE.add(apiSecretPair);
        ReliableCatFactService catFactService = new ReliableCatFactService(CHAOTICS_URL + apiSecretPair.getApiId());

        CatFact catFact = catFactService.getCatFact();

        Assert.assertEquals("Cats sleep 70% of their lives.", catFact.getText());
    }

    @Test
    public void testDefaultFactIsGivenWhenServiceTimesout() throws IOException {
        ApiSecretPair apiSecretPair = CHAOTICS_CLIENT.createEndpoint(timeoutResponse());
        APIS_TO_DELETE.add(apiSecretPair);
        ReliableCatFactService catFactService = new ReliableCatFactService(CHAOTICS_URL + apiSecretPair.getApiId());

        CatFact catFact = catFactService.getCatFact();

        Assert.assertEquals("Cats sleep 70% of their lives.", catFact.getText());
    }

    private static CreateApiRequest successfulResponse() {
        String responseBody = "{\"status\":{\"verified\":true,\"sentCount\":1},\"type\":\"cat\",\"deleted\":false,\"_id\":\"5b1b3fd8841d9700146158ce\",\"updatedAt\":\"2020-08-23T20:20:01.611Z\",\"createdAt\":\"2018-07-17T20:20:02.104Z\",\"user\":\"5a9ac18c7478810ea6c06381\",\"text\":\"Thank to an extremely efficient pair of kidneys, cats can hydrate themselves by drinking salt water.\",\"source\":\"user\",\"__v\":0,\"used\":false}";

        return CreateApiRequest.builder()
                .withApiResponses(Collections.singletonList(ApiResponse.builder()
                        .withResponseBody(responseBody)
                        .withResponseContentType(ContentType.APPLICATION_JSON.getMimeType())
                        .withResponseStatusCode(200)
                        .withCharset("UTF-8")
                        .build()))
                .build();
    }

    private CreateApiRequest unavailableResponse() {
        return CreateApiRequest.builder()
                .withApiResponses(Collections.singletonList(ApiResponse.builder()
                        .withResponseBody("service unavailable")
                        .withResponseContentType(ContentType.TEXT_PLAIN.getMimeType())
                        .withResponseStatusCode(503)
                        .withCharset("UTF-8")
                        .withLatency(1)
                        .build()))
                .build();
    }

    private CreateApiRequest timeoutResponse() {
        String responseBody = "{\n" +
                "    \"status\": {\n" +
                "        \"verified\": true,\n" +
                "        \"sentCount\": 1\n" +
                "    },\n" +
                "    \"type\": \"cat\",\n" +
                "    \"deleted\": false,\n" +
                "    \"_id\": \"591f98883b90f7150a19c2a4\",\n" +
                "    \"__v\": 0,\n" +
                "    \"text\": \"If your cat snores or rolls over on his back to expose his belly, it means he trusts you.\",\n" +
                "    \"source\": \"api\",\n" +
                "    \"updatedAt\": \"2020-08-23T20:20:01.611Z\",\n" +
                "    \"createdAt\": \"2018-01-04T01:10:54.673Z\",\n" +
                "    \"used\": false,\n" +
                "    \"user\": \"5a9ac18c7478810ea6c06381\"\n" +
                "}";

        return CreateApiRequest.builder()
                .withApiResponses(Collections.singletonList(ApiResponse.builder()
                        .withResponseBody(responseBody)
                        .withResponseContentType(ContentType.TEXT_PLAIN.getMimeType())
                        .withResponseStatusCode(200)
                        .withCharset("UTF-8")
                        .withLatency(5000)
                        .build()))
                .build();
    }

}
