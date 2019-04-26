package no.fdk.concept;

import lombok.Data;

@Data
public class Publisher {
    private String uri;
    private String identifier;
    private String name;
    private LanguageLiteral prefLabel;

    public Publisher(String orgnr) {
        this.identifier = orgnr;
        this.uri = "https://data.brreg.no/enhetsregisteret/api/enheter/" + orgnr;
    }

    public Publisher(String orgnr, String uri) {
        this.identifier = orgnr;
        this.uri = uri;
    }

    public Publisher() {
        // Default constructor needed for frameworks
    }
}