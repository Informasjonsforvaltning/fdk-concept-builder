package no.fdk.concept.builder;

import lombok.Data;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.SKOSXL;

@Data
public class ConceptBuilder {

    private Model model;
    private Resource resource;

    protected ConceptBuilder(String uri, Model model) {
        this.model = model;

        resource = model.createResource(uri)
                .addProperty(RDF.type, SKOS.Concept);
    }

    public Model build() {
        return model;
    }

    public DefinitionBuilder definitionBuilder(Resource definitionResource) {
        return new DefinitionBuilder(this, definitionResource);
    }

    public ConceptBuilder publisher(String organizationNumber) {
        Resource publisher = model.createResource("https://data.brreg.no/enhetsregisteret/api/enheter/" + organizationNumber);
        resource.addProperty(DCTerms.publisher, publisher);

        return this;
    }

    public ConceptBuilder identifier(String identifier) {
        resource.addProperty(DCTerms.identifier, identifier);

        return this;
    }

    public ConceptBuilder preferredTerm(final String term, final String language) {

        model.add(resource, SKOSXL.prefLabel, createSkosxlLabel(term, language));

        return this;
    }

    public ConceptBuilder alternativeTerm(final String term, final String language) {

        resource.addProperty(SKOSXL.altLabel, createSkosxlLabel(term, language));

        return this;
    }

    public ConceptBuilder deprecatedTerm(final String term, final String language) {

        resource.addProperty(SKOSXL.hiddenLabel, createSkosxlLabel(term, language));

        return this;
    }

    public ConceptBuilder subject(final String subject, final String language) {
        resource.addProperty(DCTerms.subject, subject, language);

        return this;
    }

    public ContactPointBuilder contactPointBuilder() {

        return new ContactPointBuilder(this);
    }

    private Resource createSkosxlLabel(final String labelText, final String language) {
        Resource resource = model.createResource(SKOSXL.Label);
        resource.addProperty(SKOSXL.literalForm, labelText, language);
        return resource;
    }

}
