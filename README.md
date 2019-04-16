# fdk-concept-builder
A stand alone library to instantiate concepts, deserialize/serialize them in RDF and Json according to [DIFIs standard](https://doc.difi.no/data/forvaltningsstandard-begrepsbeskrivelser/)

See *Forvaltningsstandard for tilgjengeliggjøring av begrepsbeskrivelser*: [begrep-skos-ap-no](https://doc.difi.no/data/begrep-skos-ap-no/)


# Usage

## Concept

```java
Model conceptModel = ConceptBuilder.builder("http://my.org/concept/application")
                .publisher("123456789")
                .definitionBuilder(SKOSNO.Definisjon)
                    .text("an application is a program", "en")
                    .source("see the law", "en", "https://lovdata.no/NL/lov/1980-02-08-2/5-1")
                    .audience("allmenheten", "nb")
                    .scopeNote("dette gjelder intill videre", "nb")
                    .modified("2017-10-20")
                    .build()
                .definitionBuilder(SKOSNO.AlternativFormulering)
                    .text("can be a program also", "en")
                    .build()
                .subject("tjenester 3.0", "no")
                .identifier("t3:application")
                .preferredTerm("application", "en")
                .preferredTerm("applikasjon", "no")
                .alternativeTerm("app", "en")
                .deprecatedTerm("service", "en")
                .deprecatedTerm("tjeneste", "no")
                .contactPointBuilder()
                    .email("me@org.no")
                    .telephone("+4755555555")
                    .build()
                .build();

        
        conceptModel.write(System.out, "TURTLE");
```

### Example output (Turtle)

```turtle
@prefix schema: <http://schema.org/> .
@prefix iso:   <http://iso.org/25012/2008/dataquality/> .
@prefix spdx:  <http://spdx.org/rdf/terms#> .
@prefix adms:  <http://www.w3.org/ns/adms#> .
@prefix skosxl: <http://www.w3.org/2008/05/skos-xl#> .
@prefix owl:   <http://www.w3.org/2002/07/owl#> .
@prefix skosno: <http://difi.no/skosno#> .
@prefix dqv:   <http://www.w3.org/ns/dqv#> .
@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
@prefix skos:  <http://www.w3.org/2004/02/skos/core#> .
@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
@prefix vcard: <http://www.w3.org/2006/vcard/ns#> .
@prefix xkos:  <http://rdf-vocabulary.ddialliance.org/xkos#> .
@prefix oa:    <http://www.w3.org/ns/prov#> .
@prefix dct:   <http://purl.org/dc/terms/> .
@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix dcatno: <http://difi.no/dcatno#> .
@prefix dcat:  <http://www.w3.org/ns/dcat#> .
@prefix foaf:  <http://xmlns.com/foaf/0.1/> .

<http://my.org/concept/application>
        a                             skos:Concept ;
        skosno:betydningsbeskrivelse  [ a           skosno:AlternativFormulering ;
                                        rdfs:label  "can be a program also"@en
                                      ] ;
        skosno:betydningsbeskrivelse  [ a               skosno:Definisjon ;
                                        rdfs:label      "an application is a program"@en ;
                                        dct:audience    "allmenheten"@nb ;
                                        dct:modified    "2017-10-20"^^xsd:date ;
                                        dct:source      [ rdfs:label    "see the law"@en ;
                                                          rdfs:seeAlso  <https://lovdata.no/NL/lov/1980-02-08-2/5-1>
                                                        ] ;
                                        skos:scopeNote  "dette gjelder intill videre"@nb
                                      ] ;
        dct:identifier                "t3:application" ;
        dct:publisher                 <https://data.brreg.no/enhetsregisteret/api/enheter/123456789> ;
        dct:subject                   "tjenester 3.0"@no ;
        skosxl:altLabel               [ a                   skosxl:Label ;
                                        skosxl:literalForm  "app"@en
                                      ] ;
        skosxl:hiddenLabel            [ a                   skosxl:Label ;
                                        skosxl:literalForm  "service"@en
                                      ] ;
        skosxl:hiddenLabel            [ a                   skosxl:Label ;
                                        skosxl:literalForm  "tjeneste"@no
                                      ] ;
        skosxl:prefLabel              [ a                   skosxl:Label ;
                                        skosxl:literalForm  "applikasjon"@no
                                      ] ;
        skosxl:prefLabel              [ a                   skosxl:Label ;
                                        skosxl:literalForm  "application"@en
                                      ] ;
        dcat:contactPoint             [ a                   vcard:Organization ;
                                        vcard:hasEmail      <mailto:me@org.no> ;
                                        vcard:hasTelephone  <tel:+4755555555>
                                      ] .
```



## ConceptCollection