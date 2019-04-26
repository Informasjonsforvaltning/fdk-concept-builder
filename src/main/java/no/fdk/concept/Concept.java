package no.fdk.concept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Concept {

    @ApiModelProperty("The uri of the concept [dct:identifier]")
    private String uri;

    @ApiModelProperty("identifier")
    private String identifier;

    private Publisher publisher;

    @ApiModelProperty("The definition [skosno:Definisjon]")
    private Definition definition;

    @ApiModelProperty("The alternative definition [skosno:Definisjon]")
    private Definition alternativeDefinition;

    @ApiModelProperty("Subject [dct:subject]")
    private LanguageLiteral subject;

    @ApiModelProperty("Preferred labels [skosxl:prefLabel]")
    private LanguageLiteral prefLabel;

    @ApiModelProperty("Alternative labels [skos:altLabel]")
    private List<LanguageLiteral> altLabel;

    @ApiModelProperty("Hidden labels [skos:hiddenLabel]")
    private List<LanguageLiteral> hiddenLabel;

    @ApiModelProperty("Contact point [dcat:contactPoint]")
    private ContactPoint contactPoint;

}