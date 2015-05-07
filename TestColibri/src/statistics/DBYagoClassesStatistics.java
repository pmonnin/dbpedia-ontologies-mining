package statistics;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

import util.Pair;
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
		Stack<Pair<DBYagoClass, Integer>> stack = new Stack<Pair<DBYagoClass, Integer>>();
		stack.push(new Pair<DBYagoClass, Integer>(yClass, 0));
		int currentDepth = 0;
		
		while(!stack.isEmpty()) {
			Pair<DBYagoClass, Integer> p = stack.pop();
			
			if((p.getValue1() == null || p.getValue1().getParentsNumber() == 0)) { 
				if(currentDepth < p.getValue2())
					currentDepth = p.getValue2();
			}
			
			else {
				for(String parent : p.getValue1().getParents())
					stack.push(new Pair<DBYagoClass, Integer>(this.yagoClasses.get(parent), p.getValue2() + 1));
			}
		}
		
		return currentDepth;
	}
}
