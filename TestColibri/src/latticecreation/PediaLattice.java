package latticecreation;

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

public class PediaLattice {

	private Lattice lattice;
	private HashMap<String, LatticeObject> objects;

	public PediaLattice() throws ParseException, IOException {
		objects = new HashMap<>();
		Relation rel = new TreeRelation();

		this.createLattice(rel);

		lattice = new HybridLattice(rel);
	}

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
                threadObjects = new ArrayList<LatticeObject>();
            }
        }
        
        for (LatticeCategoriesOntologiesThread thread : threadList) {
            try {
                thread.join();
                System.out.println("LATTICE THREAD TERMINE :)");
                for (LatticeObject obj : thread.getThreadObjects()) {
                 // We add the object to the lattice
                    obj.addToRelation(rel);
                    objects.put(obj.getName(),obj);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("FIN 2ème REQUETE CREATION LATTICE");
    }

	public void deleteFirstIterationAttributes() {
		Iterator<Concept> it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);

		if (it.hasNext()) {
			Concept c = it.next();
			if (it.hasNext()) {
				c = it.next();
			}

			Iterator<Comparable> it2 = c.getAttributes().iterator();

			while (it2.hasNext()) {
				Comparable att = it2.next();

				for (int i = 0; i < objects.size(); i++) {
					objects.get(i).deleteAttribute(att.toString());
				}
			}
		}

		Relation rel = new TreeRelation();

		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).addToRelation(rel);
		}

		lattice = new HybridLattice(rel);
	}

	public ArrayList<PediaConcept> execIterator() throws IOException,
			ParseException {
		Iterator<Edge> it = lattice.edgeIterator(Traversal.BOTTOM_ATTRSIZE);
		ArrayList<PediaConcept> res = new ArrayList<>();
		HashMap<String, Boolean> resHM = new HashMap<>();
		int j = 0;
		System.out.println("DEBUT EXEC ITERATOR");
		while (it.hasNext()) {
			if (j % 1000 == 0) {
				System.out.println("j = " + j);
			}
			j++;

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

			// On ne doit pas avoir plusieurs fois le même concept
			// We check if res contains pc1 :
			Boolean isIn = new Boolean(false);
			isIn = resHM.get(pc1);
			if (isIn == null) {

				// We add it to the array of results
				res.add(pc1);
				resHM.put(pc1.toString(), true);
			}

			// We check if res contains pc2 :
			isIn = resHM.get(pc2);
			if (isIn == null) {

				// pc2 a comme parent pc1;
				pc2.addParentPediaConcept(pc1);

				// We add it to the array of results
				res.add(pc2);
				resHM.put(pc1.toString(), true);
			} else {
				/*
				 * si pc2 est deja contenu dans la liste, on le récupère et on
				 * lui ajoute pc1 dans sa liste de parents
				 */
				// enlever les catégories de pc1 à pc2
				res.get(res.indexOf(pc2)).addParentPediaConcept(pc1);
			}
		}

		System.out.println("FIN RECONSTRUCTION LATTICE");
		return res;
	}
}