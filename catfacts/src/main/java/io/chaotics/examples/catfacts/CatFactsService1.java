package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class CatFactsService1 {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String catFactUrl;

    public CatFactsService1(String catFactUrl) {
        this.catFactUrl = catFactUrl;
    }

    public CatFact getCatFact() {
        CatFact catFact = null;
        try {
            Response response = Request.Get(catFactUrl).execute();
            catFact = OBJECT_MAPPER.readValue(response.returnResponse().getEntity().getContent(), CatFact.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return catFact;
    }
}
