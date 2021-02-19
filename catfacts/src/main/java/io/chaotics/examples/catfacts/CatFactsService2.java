package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class CatFactsService2 {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final CatFact ERROR_CAT_FACT = new CatFact("Cats sleep 70% of their lives.");

    private final String catFactUrl;

    public CatFactsService2(String catFactUrl) {
        this.catFactUrl = catFactUrl;
    }

    public CatFact getCatFact() {

        CatFact catFact = null;
        try {
            Response response = Request.Get(catFactUrl).execute();
            HttpResponse httpResponse = response.returnResponse();
            if (httpResponse.getStatusLine().getStatusCode() > 200) {
                catFact = ERROR_CAT_FACT;
            } else {
                catFact = OBJECT_MAPPER.readValue(httpResponse.getEntity().getContent(), CatFact.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return catFact;
    }
}
