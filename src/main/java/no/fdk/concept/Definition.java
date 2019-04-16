package no.fdk.concept;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Definition {
    private Map<String, String> text;
    private Map<String, String> scopeNote;

    private Source source;
    private String audience;
    private Date modified;
}
