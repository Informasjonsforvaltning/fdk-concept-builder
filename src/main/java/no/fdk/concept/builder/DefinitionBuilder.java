package no.fdk.concept.builder;

import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;

public class DefinitionBuilder {
    Resource resource;
    Model model;
    ConceptBuilder parent;

    public DefinitionBuilder(ConceptBuilder conceptBuilder, Resource definitionClass) {
        parent = conceptBuilder;
        model = conceptBuilder.getModel();
        resource = model.createResource(definitionClass);
    }

    public DefinitionBuilder source(String source, String language, String seeAlsoUrl) {
        Resource sourceResource = model.createResource();
        sourceResource.addProperty(RDFS.label, source, language);
        sourceResource.addProperty(RDFS.seeAlso, model.createResource(seeAlsoUrl));

        resource.addProperty(DCTerms.source, sourceResource);

        return this;
    }

    public DefinitionBuilder text(String text, String language) {

        resource.addProperty(RDFS.label, text, language);

        return this;
    }

    public DefinitionBuilder scopeNote(String scopeText, String language) {

        resource.addProperty(SKOS.scopeNote, scopeText, language);

        return this;
    }

    public DefinitionBuilder audience(String audienceText, String language) {

        resource.addProperty(DCTerms.audience, audienceText, language);

        return this;
    }

    public DefinitionBuilder modified(String date) {

        resource.addProperty(DCTerms.modified, model.createTypedLiteral(date, XSDDateType.XSDdate));

        return this;
    }

    public ConceptBuilder build() {

        parent.getResource().addProperty(SKOSNO.betydningsbeskrivelse, resource);

        return parent;
    }
}
