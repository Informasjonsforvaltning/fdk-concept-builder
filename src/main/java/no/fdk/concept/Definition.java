package no.fdk.concept;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Definition {
    private LanguageLiteral text;
    private LanguageLiteral scopeNote;

    private Source source;
    private LanguageLiteral audience;
    private Date modified;
}
