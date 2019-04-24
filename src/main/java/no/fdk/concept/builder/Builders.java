package no.fdk.concept.builder;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class Builders {

    private static Model model = createModel();

    public static CollectionBuilder collectionBuilder(String collectionUri) {
        return new CollectionBuilder(collectionUri, model);
    }

    public static ConceptBuilder conceptBuilder(String conceptUri) {
        return new ConceptBuilder(conceptUri, model);
    }

    private static Model createModel() {
        Model model = ModelFactory.createDefaultModel();

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

        return model;
    }

}
