package dbpediaanalyzer.factory;

import colibri.lib.Edge;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;
import dbpediaanalyzer.dbpediaobject.Category;
import dbpediaanalyzer.dbpediaobject.OntologyClass;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.dbpediaobject.YagoClass;
import dbpediaanalyzer.lattice.Concept;
import dbpediaanalyzer.lattice.Lattice;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeFactory {

    public static Lattice createLatticeFromDataSet(Map<String, Page> dataSet) {
        // Colibri lattice creation
        Relation relation = new TreeRelation(); // TODO test other relation types?

        for(String uri : dataSet.keySet()) {
            Page page = dataSet.get(uri);

            for(String r : page.getRelationships()) {
                relation.add(uri, r);
            }
        }

        colibri.lib.Lattice colibriLattice = new colibri.lib.HybridLattice(relation); // TODO test other lattice types?

        return colibriToDBPediaAnalyzerLattice(colibriLattice, dataSet);
    }

    private static Lattice colibriToDBPediaAnalyzerLattice(colibri.lib.Lattice colibriLattice, Map<String, Page> dataSet) {
        List<Concept> concepts = getDBPediaAnalyzerConceptsFromColibriLattice(colibriLattice);

        initializeLatticeAnnotations(concepts, dataSet);
        Lattice lattice = new Lattice(concepts);
        makeLatticeFinalAnnotations(lattice);

        return lattice;
    }

    private static List<Concept> getDBPediaAnalyzerConceptsFromColibriLattice(colibri.lib.Lattice colibriLattice) {
        List<Concept> concepts = new ArrayList<>();

        // Traversing colibri lattice and creating custom lattice's concepts
        Iterator<Edge> latticeIterator = colibriLattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        HashMap<colibri.lib.Concept, Concept> processed = new HashMap<>();
        while(latticeIterator.hasNext()) {
            Edge edge = latticeIterator.next();

            colibri.lib.Concept colibriUpperConcept = edge.getUpper();
            colibri.lib.Concept colibriLowerConcept = edge.getLower();

            if(!processed.containsKey(colibriUpperConcept)) {
                Concept c = new Concept(colibriUpperConcept);
                processed.put(colibriUpperConcept, c);
                concepts.add(c);
            }

            if(!processed.containsKey(colibriLowerConcept)) {
                Concept c = new Concept(colibriLowerConcept);
                processed.put(colibriLowerConcept, c);
                concepts.add(c);
            }

            Concept upperConcept = processed.get(colibriUpperConcept);
            Concept lowerConcept = processed.get(colibriLowerConcept);

            upperConcept.addChild(lowerConcept);
            lowerConcept.addParent(upperConcept);
        }

        return concepts;
    }

    private static void initializeLatticeAnnotations(List<Concept> concepts, Map<String, Page> dataSet) {
        for(Concept c : concepts) {
            if(c.getObjects().size() != 0) {
                List<Category> conceptCategories = Category.getAccessibleUpwardCategories(dataSet.get(c.getObjects().get(0)).getCategories());
                List<OntologyClass> conceptOntologyClasses = OntologyClass.getAccessibleUpwardOntologyClasses(dataSet.get(c.getObjects().get(0)).getOntologyClasses());
                List<YagoClass> conceptYagoClasses = YagoClass.getAccessibleUpwardYagoClasses(dataSet.get(c.getObjects().get(0)).getYagoClasses());

                for(int i = 1 ; i < c.getObjects().size() ; i++) {
                    conceptCategories.retainAll(Category.getAccessibleUpwardCategories(dataSet.get(c.getObjects().get(i)).getCategories()));
                    conceptOntologyClasses.retainAll(OntologyClass.getAccessibleUpwardOntologyClasses(dataSet.get(c.getObjects().get(i)).getOntologyClasses()));
                    conceptYagoClasses.retainAll(YagoClass.getAccessibleUpwardYagoClasses(dataSet.get(c.getObjects().get(i)).getYagoClasses()));
                }

                c.setCategories(conceptCategories);
                c.setOntologyClasses(conceptOntologyClasses);
                c.setYagoClasses(conceptYagoClasses);
            }
        }
    }

    private static void makeLatticeFinalAnnotations(Lattice lattice) {
        HashMap<Concept, Boolean> seen = new HashMap<>();
        seen.put(lattice.getTop(), true);
        Queue<Concept> queue = new LinkedList<>();
        queue.add(lattice.getTop());

        while(!queue.isEmpty()) {
            Concept concept = queue.poll();

            for(Concept descendant : concept.getDescendants()) {
                descendant.removeCategories(concept.getCategories());
                descendant.removeOntologyClasses(concept.getOntologyClasses());
                descendant.removeYagoClasses(concept.getYagoClasses());
            }

            for(Concept child : concept.getChildren()) {
                if(!seen.containsKey(child)) {
                    queue.add(child);
                    seen.put(child, true);
                }
            }
        }
    }
}
