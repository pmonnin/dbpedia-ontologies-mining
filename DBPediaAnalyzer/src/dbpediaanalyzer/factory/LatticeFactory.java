package dbpediaanalyzer.factory;

import colibri.lib.Relation;
import colibri.lib.TreeRelation;
import dbpediaanalyzer.dbpediaobject.Page;
import dbpediaanalyzer.lattice.Lattice;

import java.util.HashMap;

/**
 * TODO JAVADOC
 *
 * @author Pierre Monnin
 *
 */
public class LatticeFactory {

    /**
     * TODO JAVADOC
     */
    public Lattice createLatticeFromDataSet(HashMap<String, Page> dataSet) {
        // Colibri lattice creation
        Relation relation = new TreeRelation(); // TODO test other relation types?

        for(String uri : dataSet.keySet()) {
            Page page = dataSet.get(uri);

            for(String r : page.getRelationships()) {
                relation.add(uri, r);
            }
        }

        colibri.lib.Lattice colibriLattice = new colibri.lib.HybridLattice(relation); // TODO test other lattice types?

        // DBPediaAnalyzer lattice creation from colibri lattice
        return new Lattice(colibriLattice, dataSet);
    }
}
