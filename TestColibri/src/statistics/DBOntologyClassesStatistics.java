package statistics;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

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
		// Children relationship creation
		Set<String> keys = this.ontologies.keySet();
		for(String key : keys) {
			for(String parent : this.ontologies.get(key).getParents()) {
				DBOntologyClass parentOnto = this.ontologies.get(parent);
				
				if(parentOnto != null)
					parentOnto.addChildren(key);
			}
		}
		
		// Ontologies number
		this.ontologiesNumber = this.ontologies.size();
		
		// Orphans number, depth, direct and inferred subsumptions number
		for(String key : keys) {
			// Orphans
			if(this.ontologies.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;

				// Depth
				int depthOnto = computeDepth(key);
				if(depthOnto > this.depth)
					this.depth = depthOnto;
			}
		
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
		int currentDepth = 0;
		Stack<String> stack = new Stack<String>();
		stack.push(key);
		
		while(!stack.isEmpty()) {
			String ontology = stack.pop();
			DBOntologyClass dbOntology = this.ontologies.get(ontology);
			
			if(dbOntology != null) {
				if(dbOntology.getChildrenNumber() == 0 && dbOntology.getDepth() > currentDepth) {
					currentDepth = dbOntology.getDepth();
				}
				
				else {
					for(String child : dbOntology.getChildren()) {
						DBOntologyClass childCat = this.ontologies.get(child);
						
						if(childCat != null && childCat.getDepth() <= dbOntology.getDepth()) {
							childCat.setDepth(dbOntology.getDepth() + 1);
							stack.push(child);
						}
					}
				}
			}
		}
		
		return currentDepth;
	}
}
