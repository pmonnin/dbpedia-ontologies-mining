package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import latticecreation.PediaLattice;

import org.json.simple.parser.ParseException;

import dbpediaobjects.DBCategory;
import dbpediaobjects.DBOntologyClass;

public class Main {

    // Ontology :
    // http://wiki.dbpedia.org/Ontology
    // http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
    // http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
    // http://dbpedia.org/ontology/

    public static void main(String[] args) throws ParseException, IOException {
        // Crawling DB categories
        DBCategoriesCrawler dbCategoriesCrawler = new DBCategoriesCrawler();
        dbCategoriesCrawler.computeParents();
        HashMap<String, DBCategory> dbcategories = dbCategoriesCrawler.getDbcategories();

        // Crawling DB ontologies
        DBOntologyClassesCrawler dbOntologyClasses = new DBOntologyClassesCrawler();
        dbOntologyClasses.computeParents();
        HashMap<String, DBOntologyClass> dbontologies = dbOntologyClasses.getDbontologies();

        // We create the lattice
        PediaLattice lattice = new PediaLattice();
        // lattice.deleteFirstIterationAttributes();
        ArrayList<PediaConcept> lc = lattice.execIterator();

        // for(PediaConcept c : lc){
        // System.out.println("/***********Concept************/");
        // for(String obj : c.getListeObjets())
        // System.out.println("objet: "+obj);
        // System.out.println("\n");
        //
        // for(String cat : c.getCategories())
        // System.out.println("catégorie: "+cat);
        // System.out.println("\n");
        //
        // for(PediaConcept par : c.getParents()){
        // for(String op : par.getListeObjets())
        // System.out.println("objet du parent: "+op);
        // }
        // System.out.println("/***********Fin concept*********/\n\n");
        // }

        /* Pour chaque concept */
        for (PediaConcept c : lc) {
            /* Récupérer l'union des catégories des parents */
            ArrayList<String> latticeOntologyParents = c.unionCategoriesParent();
            ArrayList<String> latticeOntologies = c.getCategories();

            /* Pour chaque catégorie du concept */
            for (String latticeOntology : latticeOntologies) {
                /* Regarder si la catégorie est incluse dans les catégories de l'union */
                if (latticeOntologyParents.contains(latticeOntology)) {
                    /* YOLO à partir d'ici ! */
                    
                    DBOntologyClass dbPediaOntology = dbontologies.get(latticeOntology);
                    ArrayList<String> latticeOntologyParentsCopy = new ArrayList<String>(latticeOntologyParents);
                    ArrayList<String> dbPediaOntologyParentsCopy = new ArrayList<String>(dbPediaOntology.getParents());
                    
                    latticeOntologyParentsCopy.removeAll(dbPediaOntologyParentsCopy);
                    
                    if (!latticeOntologyParentsCopy.isEmpty()) {
                        System.out.println("Tada :) Ces parents sont dans la lattice mais pas sur dbPedia !");
                        System.out.println(latticeOntologyParentsCopy);
                    }
                    
                    latticeOntologyParentsCopy = new ArrayList<String>(latticeOntologyParents);
                    dbPediaOntologyParentsCopy.removeAll(latticeOntologyParentsCopy);
                    
                    if (!dbPediaOntologyParentsCopy.isEmpty()) {
                        System.out.println("Tada :) Ces parents sont dans dbPedia mais pas dans notre treillis !");
                        System.out.println(dbPediaOntologyParentsCopy);
                    }
                    
                    /* partie YOLO terminée ! */
                } else {

                }
            }
        }

    }
}
