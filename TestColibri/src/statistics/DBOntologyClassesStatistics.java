package statistics;

import java.util.HashMap;
import java.util.Set;

import dbpediaobjects.DBOntologyClass;
import dbpediaobjects.DBYagoClass;

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
		
		// Orphans number, depth, direct and inferred subsumptions number
		Set<String> keys = this.ontologies.keySet();
		for(String key : keys) {
			// Orphans
			if(this.ontologies.get(key).getParentsNumber() == 0)
				this.orphansNumber++;
		
			// Depth
			int depthOnto = computeDepth(key);
			if(depthOnto > this.depth)
				this.depth = depthOnto;
		
			// Direct subsumptions
			this.directSubsumptions += this.ontologies.get(key).getParentsNumber();
		}
	}
	
	public void displayStatistics() {
		System.out.println("== DB Pedia Ontology classes statistics ==");
		System.out.println("Classes number: " + this.ontologiesNumber);
		System.out.println("Direct subsomptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsomptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private int computeDepth(String key) {
		DBOntologyClass ontoClass = this.ontologies.get(key);
		
		if(ontoClass == null)
			return 0;
	
		if(ontoClass.getParentsNumber() == 0)		
			return 0;
		
		int currentDepth = 0;
		for(String parent : ontoClass.getParents()) {
			int parentDepth = computeDepth(parent);
			
			if(parentDepth > currentDepth)
				currentDepth = parentDepth;
		}
		
		return currentDepth + 1;
	}
}
