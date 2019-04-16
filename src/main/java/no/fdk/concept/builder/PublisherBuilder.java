package no.fdk.concept.builder;

import org.apache.jena.rdf.model.Resource;

public class PublisherBuilder {
    private Resource resource;
    private ConceptBuilder parent;

    PublisherBuilder(ConceptBuilder conceptBuilder, String organizationNumber) {
        parent = conceptBuilder;
    }


}
