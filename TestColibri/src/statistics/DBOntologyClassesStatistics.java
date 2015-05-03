package statistics;

import java.util.HashMap;
import java.util.Set;

import dbpediaobjects.DBOntologyClass;

/**
 * Class computing statistics over DB Ontology classes hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBOntologyClassesStatistics {
	private HashMap<String, DBOntologyClass> ontologies;
	private int ontologiesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBOntologyClassesStatistics(HashMap<String, DBOntologyClass> ontologies) {
		this.ontologies = ontologies;
		this.ontologiesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Ontologies number
		this.ontologiesNumber = this.ontologies.size();
		
		// Orphans number and direct subsumptions number
		Set<String> keys = this.ontologies.keySet();
		for(String key : keys) {
			if(this.ontologies.get(key).getParentsNumber() == 0)
				this.orphansNumber++;
			
			this.directSubsumptions += this.ontologies.get(key).getParentsNumber();
		}
		
		// Depth
	}
	
	public void displayStatistics() {
		System.out.println("== DB Pedia Ontology classes statistics ==");
		System.out.println("Classes number: " + this.ontologiesNumber);
		System.out.println("Direct subsomptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsomptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
}
