package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import LatticeCreation.ALattice;
import ServerLink.AFileReader;
import ServerLink.AJsonParser;

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
		ALattice lattice = new ALattice();
		//lattice.deleteFirstIterationAttributes();
		lattice.execIterator();
	}
}
