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
	
	public DBYagoClassesStatistics(HashMap<String, DBYagoClass> yagoClasses) {
		this.yagoClasses = yagoClasses;
		this.yagoClassesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
	}
	
	public void computeStatistics() {
		// Yago classes number
		this.yagoClassesNumber = yagoClasses.size();
		
		// Orphans number
		Set<String> keys = this.yagoClasses.keySet();
		for(String key : keys) {
			if(yagoClasses.get(key).getParentsNumber() == 0)
				this.orphansNumber++;
		}
		
		// Depth
	}
	
	public void displayStatistics() {
		System.out.println("== DB Yago classes hierarchy statistics ==");
		System.out.println("Number: " + this.yagoClassesNumber);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
}
