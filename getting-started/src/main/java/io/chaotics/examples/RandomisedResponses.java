package io.chaotics.examples;

import io.chaotics.api.ApiResponse;
import io.chaotics.api.ApiSecretPair;
import io.chaotics.api.CreateApiRequest;
import io.chaotics.client.ChaoticsClient;

import java.io.IOException;
import java.util.Arrays;

public class RandomisedResponses {

    public static void main(String[] args) throws IOException {
        ChaoticsClient chaoticsClient = new ChaoticsClient();
        ApiSecretPair apiSecretPair = chaoticsClient.createEndpoint(CreateApiRequest.builder()
                .withApiResponses(Arrays.asList(
                        ApiResponse.builder()
                                .withResponseStatusCode(200)
                                .withResponseContentType("plain/text")
                                .withCharset("UTF-8")
                                .withResponseBody("response 1")
                                .build(),
                        ApiResponse.builder()
                                .withResponseStatusCode(200)
                                .withResponseContentType("plain/text")
                                .withCharset("UTF-8")
                                .withResponseBody("response 2")
                                .build()
                ))
                .build());

        System.out.println(apiSecretPair.toString());
    }
}
