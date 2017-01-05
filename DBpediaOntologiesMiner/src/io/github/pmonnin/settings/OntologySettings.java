package io.github.pmonnin.settings;

/**
 *
 * @author Pierre Monnin
 */
public class OntologySettings {
    private String uriPrefix;
    private String creationPrefixes;
    private String creationWhereConditions;
    private String parentsPrefixes;
    private String parentsPredicate;

    public OntologySettings(String uriPrefix, String creationPrefixes, String creationWhereConditions,
                            String parentsPrefixes, String parentsPredicate) {
        this.uriPrefix = uriPrefix;
        this.creationPrefixes = creationPrefixes;
        this.creationWhereConditions = creationWhereConditions;
        this.parentsPrefixes = parentsPrefixes;
        this.parentsPredicate = parentsPredicate;
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
}
