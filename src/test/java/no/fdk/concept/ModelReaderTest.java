package no.fdk.concept;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import no.fdk.concept.builder.ModelBuilder;
import no.fdk.concept.builder.SKOSNO;
import no.fdk.concept.reader.ModelReader;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ModelReaderTest {

    @Test
    public void readAConcept() {
        Model conceptModel = ModelBuilder.builder()
                .conceptBuilder("http://my.org/concept/application")
                .publisher("123456789")
                .definitionBuilder(SKOSNO.Definisjon)
                .text("an application is a program", "en")
                .source("see the law", "en", "https://lovdata.no/NL/lov/1980-02-08-2/5-1")
                .audience("allmenheten", "nb")
                .scopeNote("dette gjelder intill videre", "nb")
                .modified("2017-10-20")
                .build()
                .definitionBuilder(SKOSNO.AlternativFormulering)
                .text("can be a program also", "en")
                .build()
                .subject("tjenester 3.0", "no")
                .identifier("t3:application")
                .preferredTerm("application", "en")
                .preferredTerm("applikasjon", "no")
                .alternativeTerm("app", "en")
                .deprecatedTerm("service", "en")
                .deprecatedTerm("tjeneste", "no")
                .contactPointBuilder()
                .email("me@org.no")
                .telephone("+4755555555")
                .build()
                .build();

        ModelReader reader = new ModelReader(conceptModel);

        List<Concept> concepts = reader.getConcepts();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        System.out.println(gson.toJson(concepts));

    }
}
