package main;

import java.io.IOException;
import org.json.simple.parser.ParseException;

import LatticeCreation.PediaLattice;

public class Main {

	// Ontology :
	// http://wiki.dbpedia.org/Ontology
	// http://mappings.dbpedia.org/index.php/How_to_edit_the_DBpedia_Ontology
	// http://mappings.dbpedia.org/server/templatestatistics/fr/?template=Infobox_Ch%C3%A2teau
	
	// http://dbpedia.org/ontology/
	// 
	
	public static void main(String[] args) throws ParseException, IOException
	{
		// We create the lattice
		PediaLattice lattice = new PediaLattice();
		//lattice.deleteFirstIterationAttributes();
		lattice.execIterator();
	}
}
