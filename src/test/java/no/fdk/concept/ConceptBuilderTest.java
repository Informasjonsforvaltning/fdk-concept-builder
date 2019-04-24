package no.fdk.concept;


import no.fdk.concept.builder.Builders;
import no.fdk.concept.builder.ConceptBuilder;
import no.fdk.concept.builder.SKOSNO;
import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;

public class ConceptBuilderTest {

    @Test
    public void testConceptBuilder() {

        Model conceptModel = Builders.conceptBuilder("http://my.org/concept/application")
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


        conceptModel.write(System.out, "TURTLE");
    }

    @Test
    public void testCollectionBuilder() {
        Model collectionModel = Builders.collectionBuilder("http://my.org/collectino/first")
                .publisher("123456789")
                .member(Builders.conceptBuilder("https://my.org/concept/term")
                        .preferredTerm("term", "en"))
                .member(Builders.conceptBuilder("https://my.org/concept/api")
                        .preferredTerm("api", "en"))
                .member(Builders.conceptBuilder("https://my.org/concept/rest"))
                .build();

        collectionModel.write(System.out, "TURTLE");
    }
}
