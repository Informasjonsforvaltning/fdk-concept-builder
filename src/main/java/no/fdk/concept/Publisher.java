package no.fdk.concept;

import lombok.Data;

import java.util.Map;

@Data
public class Publisher {
    private String uri;
    private String id;
    private String name;
    private String orgPath;
    private Map<String, String> prefLabel;

    public Publisher(String orgnr) {
        this.id = orgnr;
        this.uri = "https://data.brreg.no/enhetsregisteret/api/enheter/" + orgnr;
    }

    public Publisher(String orgnr, String uri) {
        this.id = orgnr;
        this.uri = uri;
    }

    public Publisher() {
        // Default constructor needed for frameworks
    }
}