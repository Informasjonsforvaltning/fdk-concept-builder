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


## ConceptCollection