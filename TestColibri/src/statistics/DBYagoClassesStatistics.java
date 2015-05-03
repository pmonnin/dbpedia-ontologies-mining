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
		
		// Orphans number and direct subsumptions number
		Set<String> keys = this.yagoClasses.keySet();
		for(String key : keys) {
			if(this.yagoClasses.get(key).getParentsNumber() == 0)
				this.orphansNumber++;
			
			this.directSubsumptions += this.yagoClasses.get(key).getParentsNumber();
		}
		
		// Depth
	}
	
	public void displayStatistics() {
		System.out.println("== DB Yago classes hierarchy statistics ==");
		System.out.println("Classes number: " + this.yagoClassesNumber);
		System.out.println("Direct subsomptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsomptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
}
