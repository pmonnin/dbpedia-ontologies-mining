package io.github.pmonnin.settings;

/**
 * Settings to create a context from a SPARQL Endpoint
 * @author Pierre Monnin
 */
public class ContextSettings {
    private String prefixes;
    private String whereConditions;

    public ContextSettings(String prefixes, String whereConditions) {
        this.prefixes = prefixes;
        this.whereConditions = whereConditions;
    }

    public String getPrefixes() {
        return prefixes;
    }

    public String getWhereConditions() {
        return whereConditions;
    }
}
