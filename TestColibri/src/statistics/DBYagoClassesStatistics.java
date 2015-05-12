package statistics;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

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
		// Children relationship computation
		Set<String> keys = this.yagoClasses.keySet();
		for(String key : keys) {
			for(String parent : this.yagoClasses.get(key).getParents()) {
				DBYagoClass p = this.yagoClasses.get(parent);
				
				if(p != null)
					p.addChild(key);
			}
		}
		
		// Yago classes number
		this.yagoClassesNumber = yagoClasses.size();
		
		// Orphans number, depth and direct subsumptions number
		for(String key : keys) {
			if(this.yagoClasses.get(key).getParentsNumber() == 0) {
				// Orphans
				this.orphansNumber++;
				
				// Depth
				int currentDepth = computeDepth(key);
				if(currentDepth > this.depth)
					this.depth = currentDepth;
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.yagoClasses.get(key).getParentsNumber();
			
			// Inferred subsumptions
			this.inferredSubsumptions += computeInferredSubsumptions(key);
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
		int currentDepth = 0;
		Stack<String> stack = new Stack<String>();
		stack.push(key);
		
		while(!stack.isEmpty()) {
			String yagoClass = stack.pop();
			DBYagoClass dbYagoClass = this.yagoClasses.get(yagoClass);
			
			if(dbYagoClass != null) {
				if(dbYagoClass.getChildrenNumber() == 0 && dbYagoClass.getDepth() > currentDepth) {
					currentDepth = dbYagoClass.getDepth();
				}
				
				else {
					for(String child : dbYagoClass.getChildren()) {
						DBYagoClass childClass = this.yagoClasses.get(child);
						
						if(childClass != null && childClass.getDepth() <= dbYagoClass.getDepth()) {
							childClass.setDepth(dbYagoClass.getDepth() + 1);
							stack.push(child);
						}
					}
				}
			}
		}
		
		return currentDepth;
	}
	
	private int computeInferredSubsumptions(String key) {
		int inferredSubsumptions = 0;
		Stack<String> stack = new Stack<String>();
		
		for(String parent : this.yagoClasses.get(key).getParents()) {
			stack.push(parent);
		}
		
		while(!stack.isEmpty()) {
			DBYagoClass yagoClass = this.yagoClasses.get(stack.pop());
			
			if(yagoClass != null) {
				inferredSubsumptions += yagoClass.getParentsNumber();
				
				for(String parent : yagoClass.getParents()) {
					stack.push(parent);
				}
			}
		}
		
		return inferredSubsumptions;
	}
}
