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

    private ConceptBuilder(String uri) {
        model = ModelFactory.createDefaultModel();

        model.setNsPrefix("adms", "http://www.w3.org/ns/adms#");
        model.setNsPrefix("dcat", "http://www.w3.org/ns/dcat#");
        model.setNsPrefix("dct", "http://purl.org/dc/terms/");
        model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
        model.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
        model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        model.setNsPrefix("schema", "http://schema.org/");
        model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
        model.setNsPrefix("spdx", "http://spdx.org/rdf/terms#");
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("vcard", "http://www.w3.org/2006/vcard/ns#");
        model.setNsPrefix("dqv", "http://www.w3.org/ns/dqv#");
        model.setNsPrefix("iso", "http://iso.org/25012/2008/dataquality/");
        model.setNsPrefix("oa", "http://www.w3.org/ns/prov#");
        model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        model.setNsPrefix("dcatno", "http://difi.no/dcatno#");

        model.setNsPrefix("skosxl", "http://www.w3.org/2008/05/skos-xl#");
        model.setNsPrefix("xkos", "http://rdf-vocabulary.ddialliance.org/xkos#");

        model.setNsPrefix("skosno", "http://difi.no/skosno#");

        resource = model.createResource(uri)
                .addProperty(RDF.type, SKOS.Concept);
    }

    public Model build() {
        return model;
    }

    public static ConceptBuilder builder(String uri) {
        return new ConceptBuilder(uri);
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
