package io.chaotics.examples;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import org.junit.Test;

public class CreateDogHandlerTest {

    @Test
    public void testWithChaotics() {
        CreateDogHandler createDogHandler = new CreateDogHandler("https://api.chaotics.io/endpoint?apiId=a50eb35e-b89d-4a06-8dd7-9b150a0036cf");
//        CreateDogHandler createDogHandler = new CreateDogHandler();

        APIGatewayV2HTTPEvent event = APIGatewayV2HTTPEvent.builder()
                .withBody("fluffy")
                .build();
        createDogHandler.handleRequest(event, new FakeContext());

    }
}