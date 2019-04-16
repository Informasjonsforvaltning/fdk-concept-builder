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

    @ApiModelProperty("The definition [skosno:Definisjon]")
    private Definition definition;

    @ApiModelProperty("The alternative definition [skosno:Definisjon]")
    private Definition alternativeDefinition;

    @ApiModelProperty("Subject [dct:subject]")
    private Map<String, String> subject;

    @ApiModelProperty("Preferred labels [skosxl:prefLabel]")
    private Map<String, String> prefLabel;

    @ApiModelProperty("Alternative labels [skos:altLabel]")
    private List<Map<String, String>> altLabel;

    @ApiModelProperty("Hidden labels [skos:hiddenLabel]")
    private List<Map<String, String>> hiddenLabel;

    @ApiModelProperty("Contact point [dcat:contactPoint]")
    private ContactPoint contactPoint;

}