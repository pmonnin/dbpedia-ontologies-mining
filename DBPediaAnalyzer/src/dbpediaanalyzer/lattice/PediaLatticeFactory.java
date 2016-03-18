package dbpediaanalyzer.lattice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import dbpediaanalyzer.dbpediaobject.*;

import colibri.lib.Concept;
import colibri.lib.Edge;
import colibri.lib.HybridLattice;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;
import dbpediaanalyzer.statistic.DBPediaLatticeStatistics;

/**
 * Lattice's construction
 * 
 * @author Damien Flament
 * @author Soline Blanc
 * @author Pierre Monnin
 *
 */
@Deprecated
public class PediaLatticeFactory {

    private HashMap<String, Page> dbPages;
    private ArrayList<PediaConcept> dbLattice;
    private PediaConcept top;
    private PediaConcept bottom;

    public PediaLatticeFactory(DBCategoriesManager dbcategories, DBOntologiesManager dbontologies,
                               DBYagoClassesManager dbyagoclasses) {
        this.dbPages = new HashMap<>();
        this.dbLattice = null;
        this.top = null;
        this.bottom = null;
        this.createLattice(dbcategories, dbontologies, dbyagoclasses);
    }

    /**
     * Lattice creation
     */
    private void createLattice(DBCategoriesManager dbcategories, DBOntologiesManager dbontologies,
                               DBYagoClassesManager dbyagoclasses) {
    	// Lattice objects from colibri
    	Relation rel = new TreeRelation();
    	
    	        // Lattice construction
        System.out.println("Constructing lattice... Colibri Hybrid lattice... ");
        HybridLattice lattice = new HybridLattice(rel);
        int edgesNumber = 0;
        
        	// Edges number computation
        Iterator<Edge> it = lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        while(it.hasNext()) {
           edgesNumber++;
           it.next();
        }
       
       		// Working over edges
        System.out.println("Constructing lattice... PediaLattice... ");
        it = lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        int rate = -1;
        int i = 0;
        HashMap<Concept, PediaConcept> processed = new HashMap<>();
        this.dbLattice = new ArrayList<>();

        while(it.hasNext()) {
            if((int) ((double) i / (double) edgesNumber * 100 % 100) > rate) {
                rate = (int) ((double) i / (double) edgesNumber * 100 % 100);
                System.out.print(rate + " % ... ");
            }

            Edge currentEdge = it.next();
            i++;

            // Create upper / lower concepts relation
            Concept upperConcept = currentEdge.getUpper();
            PediaConcept upperPediaConcept = processed.get(upperConcept);

            if(upperPediaConcept == null) {
                upperPediaConcept = new PediaConcept(upperConcept, this.dbPages, dbcategories, dbontologies, dbyagoclasses);
                processed.put(upperConcept, upperPediaConcept);
                this.dbLattice.add(upperPediaConcept);
            }

            Concept lowerConcept = currentEdge.getLower();
            PediaConcept lowerPediaConcept = processed.get(lowerConcept);

            if(lowerPediaConcept == null) {
                lowerPediaConcept = new PediaConcept(lowerConcept, this.dbPages, dbcategories, dbontologies, dbyagoclasses);
                processed.put(lowerConcept, lowerPediaConcept);
                this.dbLattice.add(lowerPediaConcept);
            }

            lowerPediaConcept.addParent(upperPediaConcept);
            upperPediaConcept.addChild(lowerPediaConcept);
        }

        System.out.print("\n");

        // Looking for top and bottom concepts
        System.out.println("Top and bottom detection...");
        for(PediaConcept p : this.dbLattice) {
            if(p.getParents().size() == 0) {
                this.top = p;
            }

            if(p.getChildren().size() == 0) {
                this.bottom = p;
            }
        }

        // Multiple categories / ontologies / yago classes cleaning uphill
        System.out.println("Cleaning concepts categories, ontologies, yago classes by lattice uphill traversal...");
        Queue<PediaConcept> queue = new LinkedList<>();
        queue.add(this.bottom);
        ArrayList<PediaConcept> seen = new ArrayList<>();
        seen.add(this.bottom);
        i = 0;
        rate = -1;

        while(!queue.isEmpty()) {
            PediaConcept concept = queue.poll();
            i++;

            if((int) ((double) i / (double) this.dbLattice.size() * 100 % 100) > rate) {
                rate = (int) ((double) i / (double) this.dbLattice.size() * 100 % 100);
                System.out.print(rate + " % ... ");
            }

            for(PediaConcept parent : concept.getParents()) {
                if(!seen.contains(parent)) {
                    queue.add(parent);
                    seen.add(parent);
                }

                for(DBCategory category : parent.getCategories()) {
                    concept.getCategories().remove(category);
                }

                for(DBOntology ontology : parent.getOntologies()) {
                    concept.getOntologies().remove(ontology);
                }

                for(DBYagoClass yagoClass : parent.getYagoClasses()) {
                    concept.getYagoClasses().remove(yagoClass);
                }
            }
        }

        System.out.print("\n");

        // Lattice statistics
        System.out.println("Computing lattice statistics");
        DBPediaLatticeStatistics latticeStatistics = new DBPediaLatticeStatistics();
        latticeStatistics.computeStatistics(this.dbLattice, this.top, this.bottom);
        latticeStatistics.displayStatistics();

    }

    public ArrayList<PediaConcept> getDBLattice() {
    	return this.dbLattice;
    }
}