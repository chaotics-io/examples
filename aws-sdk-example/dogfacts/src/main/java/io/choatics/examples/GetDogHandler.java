package io.choatics.examples;

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
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.net.URI;
import java.util.Map;

public class GetDogHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private static final String OVERRIDE_URL = System.getenv("DDB_OVERRIDE_URL");

    private static DynamoDbClient dynamoDbClient;

    static {
        DynamoDbClientBuilder dynamoDbClientBuilder = DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.EU_WEST_1)
                .httpClientBuilder(UrlConnectionHttpClient.builder());

        if (OVERRIDE_URL != null) {
            dynamoDbClientBuilder.endpointOverride(URI.create(OVERRIDE_URL));
        }

        dynamoDbClient = dynamoDbClientBuilder.build();
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        String id = event.getQueryStringParameters().get("id");

        GetItemResponse item = dynamoDbClient.getItem(GetItemRequest.builder()
                .key(Map.of("id", AttributeValue.builder()
                        .s(id)
                        .build()))
                .tableName("Dogs")
                .build());

        String name = item.item().get("name").s();

        return APIGatewayV2HTTPResponse.builder()
                .withBody(name)
                .withStatusCode(200)
                .build();
    }
}
