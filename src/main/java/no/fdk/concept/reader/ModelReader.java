package no.fdk.concept.reader;

import no.fdk.concept.*;
import no.fdk.concept.builder.SKOSNO;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ModelReader {

    public static final String defaultLanguage = "nb";
    private static final Logger logger = LoggerFactory.getLogger(ModelReader.class);
    private Model model;

    public ModelReader(Model model) {
        this.model = model;
    }

    public static ModelReader read(Model model) {

        return new ModelReader(model);
    }

    public Definition extractDefinition(Resource resource, Resource definitionType) {
        Definition definition = new Definition();

        List<Resource> betydningsbeskrivelser = getNamedSubPropertiesAsListOfResources(resource, SKOSNO.betydningsbeskrivelse);

        for (Resource betydningsbeskrivelse : betydningsbeskrivelser) {
            Statement betydningstype = betydningsbeskrivelse.getProperty(RDF.type);

            if (betydningstype.getObject().asResource().equals(definitionType)) {

                LanguageLiteral definitionAsLanguageLiteral = extractLanguageLiteral(betydningsbeskrivelse, RDFS.label);
                if (definitionAsLanguageLiteral != null) {
                    definition.setText(definitionAsLanguageLiteral);
                }

                LanguageLiteral noteAsLanguageLiteral = extractLanguageLiteral(betydningsbeskrivelse, SKOS.scopeNote);
                if (noteAsLanguageLiteral != null) {
                    definition.setScopeNote(noteAsLanguageLiteral);
                }

                LanguageLiteral sourceAsLanguageLiteral = extractLanguageRDFSLabelFromLabel(betydningsbeskrivelse, DCTerms.source);
                if (sourceAsLanguageLiteral != null) {
                    Source source = new Source();
                    definition.setSource(source);
                    source.setPrefLabel(sourceAsLanguageLiteral);
                }

                LanguageLiteral audience = extractLanguageLiteral(betydningsbeskrivelse, DCTerms.audience);
                if (audience != null) {
                    definition.setAudience(audience);
                }

            }
        }
        return definition;
    }

    public List<LanguageLiteral> extractLanguageLiteralFromListOfLabels(Resource resource, Property property) {
        List<LanguageLiteral> result = new ArrayList<>();

        LanguageLiteral tmp = extractLanguageLiteralFromLabel(resource, property);
        if (tmp != null) {
            result.add(tmp);
        }
        return result;
    }


    protected String extractPublisherOrgNrFromStmt(Resource publisherResource) {
        try {
            URL url = new URL(publisherResource.getURI());
            String[] parts = url.getPath().split("/");

            return (parts[parts.length - 1]);
        } catch (Exception e) {
            logger.error("Failed while trying to parse URI for publisher {}", publisherResource.getURI());
            return null;
        }
    }

    public LanguageLiteral extractLanguageLiteral(Resource resource, Property property) {
        LanguageLiteral literal = new LanguageLiteral();

        StmtIterator iterator = resource.listProperties(property);

        while (iterator.hasNext()) {
            Statement statement = iterator.next();
            String language = statement.getLanguage();
            if (language == null || language.isEmpty()) {
                language = defaultLanguage;
            }
            if (statement.getString() != null && !statement.getString().isEmpty()) {
                literal.put(language, statement.getString());
            }
        }

        if (literal.keySet().size() > 0) {
            return literal;
        }

        return null;
    }


    public LanguageLiteral extractLanguageRDFSLabelFromLabel(Resource resource, Property property) {
        Statement stmt = resource.getProperty(property);
        if (stmt == null) {
            return null;
        }
        RDFNode node = stmt.getObject();
        Resource subResource = node.asResource();

        return extractLanguageLiteral(subResource, RDFS.label);
    }


    public LanguageLiteral extractLanguageLiteralFromLabel(Resource resource, Property property) {

        LanguageLiteral result = new LanguageLiteral();
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

    public List<Resource> getNamedSubPropertiesAsListOfResources(Resource source, Property target) {
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

        concept.setPublisher(extractPublisher(conceptResource, DCTerms.publisher));

        concept.setSubject(extractLanguageLiteral(conceptResource, DCTerms.subject));

        //concept.setExample(extractLanguageLiteral(conceptResource, SKOS.example));

        concept.setPrefLabel(extractLanguageLiteralFromLabel(conceptResource, SKOSXL.prefLabel));

        concept.setHiddenLabel(extractLanguageLiteralFromListOfLabels(conceptResource, SKOSXL.hiddenLabel));

        concept.setAltLabel(extractLanguageLiteralFromListOfLabels(conceptResource, SKOSXL.altLabel));

        concept.setDefinition(extractDefinition(conceptResource, SKOSNO.Definisjon));

        concept.setAlternativeDefinition(extractDefinition(conceptResource, SKOSNO.AlternativFormulering));

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
            if (phoneStatement != null) {
                String parsedPhoneNumber = parseURIFromStatement(phoneStatement);
                contactPoint.setTelephone(parsedPhoneNumber);
            }

            Statement emailStatement = contactPointResource.getProperty(VCARD4.hasEmail);
            if (emailStatement != null) {
                String parsedEmailAddress = parseURIFromStatement(emailStatement);
                contactPoint.setEmail(parsedEmailAddress);
            }

            Statement orgUnitStatment = contactPointResource.getProperty(VCARD4.hasOrganizationUnit);
            if (orgUnitStatment != null) {
                contactPoint.setOrganizationUnit(orgUnitStatment.getObject().asLiteral().getString());
            }


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
