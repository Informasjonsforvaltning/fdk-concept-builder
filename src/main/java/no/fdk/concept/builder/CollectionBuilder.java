package no.fdk.concept.builder;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

public class CollectionBuilder {

    private Model model;
    private Resource resource;

    protected CollectionBuilder(String collectionUri, Model model) {

        this.model = model;
        resource = model.createResource(collectionUri)
                .addProperty(RDF.type, SKOS.Collection);
    }

    public CollectionBuilder member(ConceptBuilder conceptBuilder) {

        resource.addProperty(SKOS.member, conceptBuilder.getResource());

        return this;
    }

    public CollectionBuilder member(Resource conceptResource) {
        resource.addProperty(SKOS.member, conceptResource);

        return this;
    }

    public CollectionBuilder publisher(String organizationNumber) {
        Resource publisher = model.createResource("https://data.brreg.no/enhetsregisteret/api/enheter/" + organizationNumber);
        resource.addProperty(DCTerms.publisher, publisher);

        return this;
    }

    public Resource getResource() {
        return resource;
    }

    public Model build() {
        return model;
    }
}
