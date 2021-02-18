package io.chaotics.examples;

import io.chaotics.api.ApiResponse;
import io.chaotics.api.ApiSecretPair;
import io.chaotics.api.CreateApiRequest;
import io.chaotics.client.ChaoticsClient;

import java.io.IOException;
import java.util.Collections;

public class SimpleSuccessfulResponse {

    public static void main(String[] args) throws IOException {
        ChaoticsClient chaoticsClient = new ChaoticsClient();
        ApiSecretPair apiSecretPair = chaoticsClient.createEndpoint(CreateApiRequest.builder()
                .withApiResponses(Collections.singletonList(ApiResponse.builder()
                        .withResponseStatusCode(200)
                        .withResponseContentType("text/plain")
                        .withCharset("UTF-8")
                        .withResponseBody("ok")
                        .build()))
                .build());

        System.out.println(apiSecretPair.toString());
    }
}
