package dbpediaanalyzer.lattice;

import colibri.lib.Edge;
import colibri.lib.Traversal;
import dbpediaanalyzer.dbpediaobject.*;

import java.util.*;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class Lattice {
    private ArrayList<Concept> concepts;
    private Concept top;
    private Concept bottom;

    public Lattice(colibri.lib.Lattice colibriLattice, HashMap<String, Page> dataSet) {
        this.concepts = new ArrayList<>();

        // Traversing colibri lattice and creating custom lattice
        Iterator<Edge> latticeIterator = colibriLattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        HashMap<colibri.lib.Concept, Concept> processed = new HashMap<>();
        while(latticeIterator.hasNext()) {
            Edge edge = latticeIterator.next();

            colibri.lib.Concept colibriUpperConcept = edge.getUpper();
            colibri.lib.Concept colibriLowerConcept = edge.getLower();

            if(!processed.containsKey(colibriUpperConcept)) {
                Concept c = new Concept(colibriUpperConcept);
                processed.put(colibriUpperConcept, c);
                this.concepts.add(c);
            }

            if(!processed.containsKey(colibriLowerConcept)) {
                Concept c = new Concept(colibriLowerConcept);
                processed.put(colibriLowerConcept, c);
                this.concepts.add(c);
            }

            Concept upperConcept = processed.get(colibriUpperConcept);
            Concept lowerConcept = processed.get(colibriLowerConcept);

            upperConcept.addChild(lowerConcept);
            lowerConcept.addParent(upperConcept);
        }

        topBottomInit();
        makeLatticeAnnotations(dataSet);
    }

    public Lattice(ArrayList<Concept> concepts) {
        this.concepts = new ArrayList<>(concepts);
        topBottomInit();
    }

    private void makeLatticeAnnotations(HashMap<String, Page> dataSet) {
        // Initial annotations
        for(Concept c : this.concepts) {
            if(c.getObjects().size() != 0) {
                ArrayList<Category> conceptCategories = Category.getAccessibleUpwardCategories(dataSet.get(c.getObjects().get(0)).getCategories());
                ArrayList<OntologyClass> conceptOntologyClasses = OntologyClass.getAccessibleUpwardOntologyClasses(dataSet.get(c.getObjects().get(0)).getOntologyClasses());
                ArrayList<YagoClass> conceptYagoClasses = YagoClass.getAccessibleUpwardYagoClasses(dataSet.get(c.getObjects().get(0)).getYagoClasses());

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

        // Removing duplicated categories / ontology classes / yago classes
        HashMap<Concept, Boolean> seen = new HashMap<>();
        seen.put(this.top, true);
        Queue<Concept> queue = new LinkedList<>();
        queue.add(this.top);

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

    private void topBottomInit() {
        for(Concept c : this.concepts) {
            if(c.getParents().size() == 0) {
                this.top = c;
            }

            if(c.getChildren().size() == 0) {
                this.bottom = c;
            }
        }
    }

    public Concept getTop() {
        return this.top;
    }

    public Concept getBottom() {
        return this.bottom;
    }

    public ArrayList<Concept> getConcepts() {
        return new ArrayList<>(this.concepts);
    }
}
