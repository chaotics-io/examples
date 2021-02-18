package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class SimpleCatFactService implements CatFactService {

    private static final String CAT_FACT_URL = System.getenv("CAT_FACT_URL");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public CatFact getCatFact() {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(CAT_FACT_URL);
        CatFact catFact = null;
        try {
            HttpResponse httpResponse = httpclient.execute(httpGet);
            catFact = OBJECT_MAPPER.readValue(httpResponse.getEntity().getContent(), CatFact.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return catFact;
    }
}
