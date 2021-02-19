package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

public class CatFactsService0 {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public CatFact getCatFact() {
        CatFact catFact = null;
        try {
            Response response = Request.Get("https://cat-fact.herokuapp.com/facts/random")
                    .execute();
            catFact = OBJECT_MAPPER.readValue(response.returnResponse().getEntity().getContent(), CatFact.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return catFact;
    }
}
