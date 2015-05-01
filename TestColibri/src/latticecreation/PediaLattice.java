package latticecreation;

import Statistics.LatticeStats;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import main.PediaConcept;

import org.json.simple.parser.ParseException;

import serverlink.JsonParser;
import serverlink.URLReader;
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
    private HashMap<String, LatticeObject> objects;
    private LatticeStats latticeStats;

    public PediaLattice() throws ParseException, IOException {
        latticeStats = new LatticeStats();
        objects = new HashMap<>();
        Relation rel = new TreeRelation();

        this.createLattice(rel);

        lattice = new HybridLattice(rel);
    }

    /**
     * Creation of lattice
     * @param rel 
     * @throws ParseException
     * @throws IOException 
     */
    public void createLattice(Relation rel) throws ParseException, IOException {
        URLReader urlReader = new URLReader();

        String jsonResponse = urlReader.getJSON(URLEncoder.encode("select distinct ?chose where { ?chose a <http://www.w3.org/2002/07/owl#Thing> } LIMIT 1000 ", "UTF-8"));

        System.out.println("FIN 1ère REQUETE CREATION LATTICE");
        // We parse it to get the different results
        JsonParser parser = new JsonParser(jsonResponse);
        ArrayList<String> results = parser.getResults("chose");

        int keySize = results.size();
        ArrayList<LatticeCategoriesOntologiesThread> threadList = new ArrayList<LatticeCategoriesOntologiesThread>();
        int nbCores = Runtime.getRuntime().availableProcessors();
        ArrayList<LatticeObject> threadObjects = new ArrayList<LatticeObject>();

        // Add relationship
        for (int i = 0; i < results.size(); i++) {
            LatticeObject obj = new LatticeObject(results.get(i));
            threadObjects.add(obj);

            if (i % Math.ceil(keySize / nbCores) == 0 && i != 0) {
                LatticeCategoriesOntologiesThread thread = new LatticeCategoriesOntologiesThread(threadObjects);
                thread.start();
                threadList.add(thread);
                threadObjects = new ArrayList<>();
            }
        }

        for (LatticeCategoriesOntologiesThread thread : threadList) {
            try {
                thread.join();
                System.out.println("LATTICE THREAD TERMINE :)");
                for (LatticeObject obj : thread.getThreadObjects()) {
                    // We add the object to the lattice
                    obj.addToRelation(rel);
                    objects.put(obj.getName(), obj);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("FIN 2ème REQUETE CREATION LATTICE");
    }

    public ArrayList<PediaConcept> execIterator() throws IOException, ParseException {
        Iterator<Edge> it = lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
        ArrayList<PediaConcept> res = new ArrayList<>();
        HashMap<String, Boolean> resHM = new HashMap<>();
        
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
            PediaConcept pc1 = new PediaConcept(obj1, att1, objects);

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
            PediaConcept pc2 = new PediaConcept(obj2, att2, objects);

            
            /* We must not have the same concept several times so we check 
             * if res doesn't contain pc1 and pc2 yet, before adding them
             */
            Boolean isIn = resHM.get(pc1.toString());
            if (isIn == null) {
                // We add it to the array of results
                res.add(pc1);
                resHM.put(pc1.toString(), true);
            }

            // We check if res contains pc2
            isIn = resHM.get(pc2.toString());
            if (isIn == null) {
                // pc2 is the child of pc1
                pc2.addParentPediaConcept(pc1);

                // We add it to the array of results
                res.add(pc2);
                resHM.put(pc1.toString(), true);
            } else {
                /*
                 * Otherwise, pc2 is already contained in the list, so we get it and we add pc1
                 * in its list of parents
                 */
                res.get(res.indexOf(pc2)).addParentPediaConcept(pc1);
            }
        }

        System.out.println("End exex iterator. The reconstruction of lattice is now finished!");
        latticeStats.setNbConcept(res.size()); 
        return res;
    }
}