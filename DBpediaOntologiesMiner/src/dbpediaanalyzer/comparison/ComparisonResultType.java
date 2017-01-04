package dbpediaanalyzer.comparison;

/**
 * Type of a comparison result between the annotated lattice axioms and the existing ontologies
 *
 * @author Pierre Monnin
 *
 */
public enum ComparisonResultType {
    CONFIRMED_DIRECT,
    PROPOSED_INFERRED_TO_DIRECT,
    PROPOSED_NEW
}
