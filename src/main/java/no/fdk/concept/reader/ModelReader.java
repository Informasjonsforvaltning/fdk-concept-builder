package no.fdk.concept.reader;

import no.fdk.concept.Concept;
import no.fdk.concept.ContactPoint;
import no.fdk.concept.Definition;
import no.fdk.concept.Publisher;
import no.fdk.concept.Source;
import no.fdk.concept.builder.SKOSNO;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.SKOSXL;
import org.apache.jena.vocabulary.VCARD4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModelReader {

    public static final String defaultLanguage = "nb";
    private static final Logger logger = LoggerFactory.getLogger(ModelReader.class);
    private Model model;

    private Map<String, Concept> concepts;

    public ModelReader(Model model) {
        this.model = model;
    }

    public static ModelReader read(Model model) {

        return new ModelReader(model);
    }

    public Definition extractDefinition(Resource resource) {
        Definition definition = new Definition();
        definition.setText(new HashMap());
        definition.setScopeNote(new HashMap());
        Source source = new Source();
        definition.setSource(source);

        List<Resource> betydningsbeskivelses = getNamedSubPropertiesAsListOfResources(resource, SKOSNO.betydningsbeskrivelse);

        for (Resource betydningsbeskrivelse : betydningsbeskivelses) {
            //We may need to merge the different language strings from the different betydningsbeskrivelses
            Map<String, String> definitionAsLanguageLiteral = extractLanguageLiteral(betydningsbeskrivelse, RDFS.label);
            if (definitionAsLanguageLiteral != null) {
                definition.getText().putAll(definitionAsLanguageLiteral);
            }

            Map<String, String> noteAsLanguageLiteral = extractLanguageLiteral(betydningsbeskrivelse, SKOS.scopeNote);
            if (noteAsLanguageLiteral != null) {
                definition.getScopeNote().putAll(noteAsLanguageLiteral);
            }

            Map<String, String> sourceAsLanguageLiteral = extractLanguageRDFSLabelFromLabel(betydningsbeskrivelse, DCTerms.source);
            if (sourceAsLanguageLiteral != null) {
                if (source.getPrefLabel() == null) {
                    source.setPrefLabel(new HashMap());
                }

                definition.getSource().getPrefLabel().putAll(sourceAsLanguageLiteral);
            }
        }
        return definition;
    }

    public static List<Map<String, String>> extractLanguageLiteralFromListOfLabels(Resource resource, Property property) {
        List<Map<String, String>> result = new ArrayList<>();

        Map<String, String> tmp = extractLanguageLiteralFromLabel(resource, property);
        if (tmp != null) {
            result.add(tmp);
        }
        return result;
    }


    protected static String extractPublisherOrgNrFromStmt(Resource publisherResource) {
        try {
            URL url = new URL(publisherResource.getURI());
            String[] parts = url.getPath().split("/");

            return (parts[parts.length - 1]);
        } catch (Exception e) {
            logger.error("Failed while trying to parse URI for publisher {}", publisherResource.getURI());
            return null;
        }
    }

    public static Map<String, String> extractLanguageLiteral(Resource resource, Property property) {
        Map<String, String> map = new HashMap<>();

        StmtIterator iterator = resource.listProperties(property);

        while (iterator.hasNext()) {
            Statement statement = iterator.next();
            String language = statement.getLanguage();
            if (language == null || language.isEmpty()) {
                language = defaultLanguage;
            }
            if (statement.getString() != null && !statement.getString().isEmpty()) {
                map.put(language, statement.getString());
            }
        }

        if (map.keySet().size() > 0) {
            return map;
        }

        return null;
    }

    public static Map<String, String> extractLanguageRDFSLabelFromLabel(Resource resource, Property property) {
        Statement stmt = resource.getProperty(property);
        if (stmt == null) {
            return null;
        }
        RDFNode node = stmt.getObject();
        Resource subResource = node.asResource();

        return extractLanguageLiteral(subResource, RDFS.label);
    }


    public static Map<String, String> extractLanguageLiteralFromLabel(Resource resource, Property property) {

        Map<String, String> result = new HashMap<>();
        StmtIterator iterator = resource.listProperties(property);

        while (iterator.hasNext()) {
            Statement statement = iterator.next();

            if (statement == null) {
                return null;
            }
            RDFNode node = statement.getObject();
            Resource subResource = node.asResource();

            result.putAll(extractLanguageLiteral(subResource, SKOSXL.literalForm));
        }

        return result;
    }

    public static List<Resource> getNamedSubPropertiesAsListOfResources(Resource source, Property target) {
        List<Resource> resources = new ArrayList<>();
        StmtIterator iterator = source.listProperties(target);
        while (iterator.hasNext()) {
            Statement stmt = iterator.next();
            resources.add(stmt.getObject().asResource());
        }
        return resources;
    }

    public List<Concept> getConcepts() {

        List<Concept> concepts = new ArrayList<>();

        ResIterator conceptIterator = model.listResourcesWithProperty(RDF.type, SKOS.Concept);

        while (conceptIterator.hasNext()) {
            Resource conceptResource = conceptIterator.nextResource();
            concepts.add(extractConceptFromModel(conceptResource));
        }
        return concepts;
    }

    public Concept extractConceptFromModel(Resource conceptResource) {
        Concept concept = new Concept();

        concept.setUri(conceptResource.getURI());//So that URI is actually addressable into our system.

        concept.setIdentifier(conceptResource.getProperty(DCTerms.identifier).getString());

        //concept.setPublisher(extractPublisher(conceptResource, DCTerms.publisher));

        concept.setSubject(extractLanguageLiteral(conceptResource, DCTerms.subject));

        //concept.setExample(extractLanguageLiteral(conceptResource, SKOS.example));

        concept.setPrefLabel(extractLanguageLiteralFromLabel(conceptResource, SKOSXL.prefLabel));

        concept.setHiddenLabel(extractLanguageLiteralFromListOfLabels(conceptResource, SKOSXL.hiddenLabel));

        concept.setAltLabel(extractLanguageLiteralFromListOfLabels(conceptResource, SKOSXL.altLabel));

        concept.setDefinition(extractDefinition(conceptResource));

        concept.setContactPoint(extractContactPoint(conceptResource));

        return concept;
    }

    private ContactPoint extractContactPoint(Resource resource) {
        ContactPoint contactPoint = new ContactPoint();

        try {
            Statement propertyStmnt = resource.getProperty(DCAT.contactPoint);
            if (propertyStmnt == null) {
                return null;
            }

            Resource contactPointResource = propertyStmnt.getObject().asResource();

            Statement phoneStatement = contactPointResource.getProperty(VCARD4.hasTelephone);
            String parsedPhoneNumber = parseURIFromStatement(phoneStatement);
            contactPoint.setTelephone(parsedPhoneNumber);

            Statement emailStatement = contactPointResource.getProperty(VCARD4.hasEmail);
            String parsedEmailAddress = parseURIFromStatement(emailStatement);
            contactPoint.setEmail(parsedEmailAddress);

        } catch (Exception e) {
            logger.warn("Error when extracting property {} from resource {}", DCAT.contactPoint, resource.getURI(), e);
        }

        return contactPoint;
    }

    private String parseURIFromStatement(Statement statement) {
        if (statement.getResource().isURIResource()) {
            try {
                URI uri = new URI(statement.getResource().getURI());
                return uri.getSchemeSpecificPart();
                //contactPoint.setEmail(uri.getSchemeSpecificPart());
            } catch (URISyntaxException use) {
                logger.error("Email URI not parsable :" + statement.getObject().toString());
            }
        }
        return "";
    }

    public Publisher extractPublisher(Resource resource, Property property) {
        try {
            Statement propertyStmnt = resource.getProperty(property);
            if (propertyStmnt != null) {
                Resource publisherResource = resource.getModel().getResource(propertyStmnt.getObject().asResource().getURI());
                String orgNr = extractPublisherOrgNrFromStmt(publisherResource);
                return new Publisher(orgNr);
            }
        } catch (Exception e) {
            logger.warn("Error when extracting property {} from resource {}", DCTerms.publisher, resource.getURI(), e);
        }

        return null;
    }
}
