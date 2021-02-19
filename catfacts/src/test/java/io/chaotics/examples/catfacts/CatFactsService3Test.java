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

public class CatFactsService3Test {

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
    public void testDefaultFactIsGivenWhenServiceTimesout() throws IOException {
        ApiSecretPair apiSecretPair = CHAOTICS_CLIENT.createEndpoint(timeoutResponse());
        APIS_TO_DELETE.add(apiSecretPair);
        CatFactsService3 catFactService = new CatFactsService3(CHAOTICS_URL + apiSecretPair.getApiId());

        CatFact catFact = catFactService.getCatFact();

        Assert.assertEquals("Cats sleep 70% of their lives.", catFact.getText());
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
                        .withResponseContentType(ContentType.APPLICATION_JSON.getMimeType())
                        .withResponseStatusCode(200)
                        .withCharset("UTF-8")
                        .withLatency(5000)
                        .build()))
                .build();
    }
}