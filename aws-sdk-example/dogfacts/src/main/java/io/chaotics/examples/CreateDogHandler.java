package io.chaotics.examples;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class CreateDogHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static DynamoDbClient dynamoDbClient;

    public CreateDogHandler() {
        dynamoDbClient = DynamoDbClient.create();
    }

    public CreateDogHandler(String overrideUrl) {
        dynamoDbClient = DynamoDbClient.builder()
//                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
//                .region(Region.EU_WEST_1)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .endpointOverride(URI.create(overrideUrl))
                .build();
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        String name = event.getBody();

        String id = UUID.randomUUID().toString();
        Map<String, AttributeValue> item = Map.of("id", s(id),
                                                  "name", s(name));
        try {
            dynamoDbClient.putItem(PutItemRequest.builder()
                    .item(item)
                    .tableName("Dogs")
                    .build());
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
        }

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(200)
                .withBody(id)
                .build();
    }

    private AttributeValue s(String s) {
        return AttributeValue.builder().s(s).build();
    }
}
