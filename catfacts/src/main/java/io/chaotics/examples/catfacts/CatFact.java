package io.chaotics.examples.catfacts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatFact {

    private String text;

    public CatFact() {
    }

    public CatFact(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
