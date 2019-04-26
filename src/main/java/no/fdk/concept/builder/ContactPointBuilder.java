package no.fdk.concept.builder;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.VCARD4;

public class ContactPointBuilder {
    private ConceptBuilder parent;
    private Resource resource;

    ContactPointBuilder(ConceptBuilder parent) {
        this.parent = parent;
        resource = parent.getModel().createResource(VCARD4.Organization);
    }

    public ContactPointBuilder email(String email) {
        resource.addProperty(VCARD4.hasEmail, emailResource(email));
        return this;
    }

    public ContactPointBuilder telephone(String telephone) {
        resource.addProperty(VCARD4.hasTelephone, telephoneResource(telephone));

        return this;
    }

    public ContactPointBuilder organizationUnit(String orgUnit) {
        resource.addProperty(VCARD4.hasOrganizationUnit, orgUnit);

        return this;
    }

    public ConceptBuilder build() {
        parent.getResource().addProperty(DCAT.contactPoint, resource);

        return parent;
    }

    private Resource emailResource(String email) {
        return parent.getModel().createResource("mailto:" + email);
    }

    private Resource telephoneResource(String telephone) {
        return parent.getModel().createResource("tel:" + telephone);
    }

}
