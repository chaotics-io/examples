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

public class CatFactsService2Test {

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
    public void testDefaultFactIsGivenWhenServiceUnavailable() throws IOException {
        ApiSecretPair apiSecretPair = CHAOTICS_CLIENT.createEndpoint(unavailableResponse());
        APIS_TO_DELETE.add(apiSecretPair);
        CatFactsService2 catFactService = new CatFactsService2(CHAOTICS_URL + apiSecretPair.getApiId());

        CatFact catFact = catFactService.getCatFact();

        Assert.assertEquals("Cats sleep 70% of their lives.", catFact.getText());
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
}