package io.chaotics.examples;

import io.chaotics.api.ApiResponse;
import io.chaotics.api.ApiSecretPair;
import io.chaotics.api.CreateApiRequest;
import io.chaotics.client.ChaoticsClient;

import java.io.IOException;
import java.util.Collections;

public class App {

    public static void main(String[] args) {
        ChaoticsClient chaoticsClient = new ChaoticsClient();
        try {
            ApiSecretPair apiSecretPair = chaoticsClient.createEndpoint(CreateApiRequest.builder()
                    .withApiResponses(Collections.singletonList(ApiResponse.builder()
                            .withResponseStatusCode(500)
                            .withLatency(5 * 1000)
                            .build()))
                    .build());

            System.out.println(apiSecretPair.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
