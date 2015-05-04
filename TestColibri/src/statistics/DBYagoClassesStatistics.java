package statistics;

import java.util.HashMap;
import java.util.Set;

import dbpediaobjects.DBYagoClass;

/**
 * Class computing statistics over DB Yago classes hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBYagoClassesStatistics {
	private HashMap<String, DBYagoClass> yagoClasses;
	private int yagoClassesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBYagoClassesStatistics(HashMap<String, DBYagoClass> yagoClasses) {
		this.yagoClasses = yagoClasses;
		this.yagoClassesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Yago classes number
		this.yagoClassesNumber = yagoClasses.size();
		
		// Orphans number, depth, direct and inferred subsumptions number
		Set<String> keys = this.yagoClasses.keySet();
		for(String key : keys) {
			// Orphans
			if(this.yagoClasses.get(key).getParentsNumber() == 0)
				this.orphansNumber++;
			
			// Depth
			int depthYago = computeDepth(key);
			if(depthYago > this.depth)
				this.depth = depthYago;
			
			// Direct subsumptions
			this.directSubsumptions += this.yagoClasses.get(key).getParentsNumber();
		}
	}
	
	public void displayStatistics() {
		System.out.println("== DB Yago classes hierarchy statistics ==");
		System.out.println("Classes number: " + this.yagoClassesNumber);
		System.out.println("Direct subsomptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsomptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private int computeDepth(String key) {
		DBYagoClass yClass = this.yagoClasses.get(key);
		
		if(yClass == null)
			return 0;
	
		if(yClass.getParentsNumber() == 0)		
			return 0;
		
		int currentDepth = 0;
		for(String parent : yClass.getParents()) {
			int parentDepth = computeDepth(parent);
			
			if(parentDepth > currentDepth)
				currentDepth = parentDepth;
		}
		
		return currentDepth + 1;
	}
}
