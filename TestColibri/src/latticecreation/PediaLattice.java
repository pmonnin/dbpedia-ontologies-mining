package latticecreation;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import main.PediaConcept;

import org.json.simple.parser.ParseException;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import statistics.LatticeStats;
import colibri.lib.Concept;
import colibri.lib.Edge;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;

/**
 * Lattice's construction
 * 
 * @author Damien Flament
 * @author Soline Blanc
 *
 */
public class PediaLattice {

    private Lattice lattice;
    private HashMap<String, DBPage> objects;
    private LatticeStats latticeStats;

    public PediaLattice() throws ParseException, IOException {
        this.latticeStats = new LatticeStats();
        this.objects = new HashMap<>();
        Relation rel = new TreeRelation();

        this.createLattice(rel);

        this.lattice = new HybridLattice(rel);
    }

    /**
     * Lattice creation
     * @param rel object representing relationship matrix
     * @throws IOException if pages cannot be crawled
     */
    public void createLattice(Relation rel) throws IOException {
    	// Ask for pages related to dbo:Person
    	System.out.println("Crawling pages related to dbo:Person hierarchy...");
        List<ChildAndParent> pages = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
                + "PREFIX dbo:<http://dbpedia.org/ontology/>  "
                + "select distinct ?child where {"
                + "?child dbo:wikiPageID ?pageId ."
                + "?child rdf:type/rdfs:subClassOf* dbo:Person ."
                + "}", "UTF-8"));
        
        // For each page we get
        System.out.print("Getting information for each page... ");
        int rate = -1;
        int i = 0;
        while(i < pages.size()) {
        	if((int) ((double) i / (double) pages.size() * 100 % 100) > rate) {
        		rate = (int) ((double) i / (double) pages.size() * 100 % 100);
        		System.out.println(rate + " % ... ");
        	}
        	
        	try {
	        	DBPage page = new DBPage(pages.get(i).getChild().getValue());
	        	
	        	// Relationships + addition to lattice relation
	        	List<ChildAndParent> relationships = JSONReader.getChildrenAndParents(URLEncoder.encode(
	        			"select distinct ?child where {"
	        			+ "<" + page.getURI() + "> ?child ?other ."
	        			+ "}", "UTF-8"));
	        	
	        	for(ChildAndParent r : relationships) {
	        		page.addRelationship(r.getChild().getValue());
	        		rel.add(page.getURI(), r.getChild().getValue());
	        	}
	        	
	        	// Categories
	        	List<ChildAndParent> categories = JSONReader.getChildrenAndParents(URLEncoder.encode(
	        			"select distinct ?", "UTF-8"));
	        	
	        	// Ontologies
	        	
	        	// Yago classes
	        	
	        	i++;
	        	this.objects.put(page.getURI(), page);
        	}
        	
        	catch(IOException e) {
        		System.err.println("Error while trying to get info on: " + pages.get(i) + ". New try...");
        	}
        }
        
    	System.out.println("Selected pages number: " + pages.size());
    }

    public ArrayList<PediaConcept> execIterator() throws IOException, ParseException {
        Iterator<Edge> it = this.lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        ArrayList<PediaConcept> res = new ArrayList<>();
        HashMap<PediaConcept, Boolean> resHM = new HashMap<>();
        
        System.out.println("BEGIN EXEC ITERATOR");
        while (it.hasNext()) {
            Edge e = it.next();
            
            // We take the 1st object
            Concept c = e.getUpper();
            Iterator<Comparable> ite = c.getObjects().iterator();
            ArrayList<String> obj1 = new ArrayList<>();
            ArrayList<String> att1 = new ArrayList<>();
           
            while (ite.hasNext()) {
                String comp = (String) ite.next();
                obj1.add(comp);
            }

            ite = c.getAttributes().iterator();

            while (ite.hasNext()) {
                String comp = (String) ite.next();
                att1.add(comp);
            }
            PediaConcept pc1 = new PediaConcept(obj1, att1, this.objects);

            // We take the 2nd object
            c = e.getLower();
            ite = c.getObjects().iterator();
            ArrayList<String> obj2 = new ArrayList<>();
            ArrayList<String> att2 = new ArrayList<>();
            
            while (ite.hasNext()) {
                String comp = (String) ite.next();
                obj2.add(comp);
            }

            ite = c.getAttributes().iterator();

            while (ite.hasNext()) {
                String comp = (String) ite.next();
                att2.add(comp);
            }
            PediaConcept pc2 = new PediaConcept(obj2, att2, this.objects);

            
            /* We must not have the same concept several times so we check 
             * if res doesn't contain pc1 and pc2 yet, before adding them
             */
            Boolean isIn = resHM.get(pc1);
            if (isIn == null) {
                // We add it to the array of results
                res.add(pc1);
                resHM.put(pc1, true);
            }

            // We check if res contains pc2
            isIn = resHM.get(pc2);
            if (isIn == null) {
                // pc2 is the child of pc1
                pc2.addParentPediaConcept(pc1);

                // We add it to the array of results
                res.add(pc2);
                resHM.put(pc2, true);
            } else {
                /*
                 * Otherwise, pc2 is already contained in the list, so we get it and we add pc1
                 * in its list of parents
                 */
                res.get(res.indexOf(pc2)).addParentPediaConcept(pc1);
            }
        }

        System.out.println("End exex iterator. The reconstruction of lattice is now finished!");
        this.latticeStats.setNbConcept(res.size()); 
        return res;
    }
}