package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

public class CatFactsService3 {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final CatFact ERROR_CAT_FACT = new CatFact("Cats sleep 70% of their lives.");

    private final String catFactUrl;

    public CatFactsService3(String catFactUrl) {
        this.catFactUrl = catFactUrl;
    }

    public CatFact getCatFact() {
        CatFact catFact;

        try {
            Response response = Request.Get(catFactUrl)
                    .socketTimeout(4 * 1000)
                    .connectTimeout(4 * 1000)
                    .execute();
            HttpResponse httpResponse = response.returnResponse();
            if (httpResponse.getStatusLine().getStatusCode() > 200) {
                catFact = ERROR_CAT_FACT;
            } else {
                catFact = OBJECT_MAPPER.readValue(httpResponse.getEntity().getContent(), CatFact.class);
            }
        } catch (IOException e) {
            catFact = ERROR_CAT_FACT;
        }

        return catFact;
    }
}
