package io.github.pmonnin.settings;

/**
 * Settings to query an ontology from a SPARQL Endpoint
 * @author Pierre Monnin
 */
public class OntologySettings {
    private String uriPrefix;
    private String creationPrefixes;
    private String creationWhereConditions;
    private String parentsPrefixes;
    private String parentsPredicate;
    private String typePrefixes;
    private String typePredicate;

    public OntologySettings(String uriPrefix, String creationPrefixes, String creationWhereConditions,
                            String parentsPrefixes, String parentsPredicate, String typePrefixes,
                            String typePredicate) {
        this.uriPrefix = uriPrefix;
        this.creationPrefixes = creationPrefixes;
        this.creationWhereConditions = creationWhereConditions;
        this.parentsPrefixes = parentsPrefixes;
        this.parentsPredicate = parentsPredicate;
        this.typePrefixes = typePrefixes;
        this.typePredicate = typePredicate;
    }

    public String getUriPrefix() {
        return this.uriPrefix;
    }

    public String getCreationPrefixes() {
        return this.creationPrefixes;
    }

    public String getCreationWhereConditions() {
        return this.creationWhereConditions;
    }

    public String getParentsPrefixes() {
        return this.parentsPrefixes;
    }

    public String getParentsPredicate() {
        return this.parentsPredicate;
    }

    public String getTypePrefixes() {
        return this.typePrefixes;
    }

    public String getTypePredicate() {
        return this.typePredicate;
    }
}
