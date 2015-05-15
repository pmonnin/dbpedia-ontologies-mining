package statistics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import serverlink.ChildAndParent;
import serverlink.JSONReader;
import util.Pair;
import dbpediaobjects.DBCategory;

/**
 * Class computing statistics over the DB Categories hierarchy
 * 
 * @author Pierre Monnin
 *
 */
public class DBCategoriesStatistics {
	private HashMap<String, DBCategory> categories;
	private int categoriesNumber;
	private int orphansNumber;
	private int depth;
	private int directSubsumptions;
	private int inferredSubsumptions;
	
	public DBCategoriesStatistics(HashMap<String, DBCategory> categories) {
		this.categories = categories;
		this.categoriesNumber = 0;
		this.orphansNumber = 0;
		this.depth = 0;
		this.directSubsumptions = 0;
		this.inferredSubsumptions = 0;
	}
	
	public void computeStatistics() {
		// Children relationship creation
		Set<String> keys = this.categories.keySet();
		for(String key : keys) {
			for(String parent : this.categories.get(key).getParents()) {
				DBCategory parentCat = this.categories.get(parent);
				
				if(parentCat != null)
					parentCat.addChild(key);
			}
		}
		
		// Categories number
		this.categoriesNumber = this.categories.size();
		
		// Orphans number, direct subsumptions number,
		ArrayList<String> orphans = new ArrayList<>(); 
		for(String key : keys) {			
			// Orphans
			if(this.categories.get(key).getParentsNumber() == 0) {
				this.orphansNumber++;
				orphans.add(key);
			}
			
			// Direct subsumptions
			this.directSubsumptions += this.categories.get(key).getParentsNumber();
		}
		
		// Depth & inferred subsumptions number
		ArrayList<String> arrayKeys = new ArrayList<String>(keys);
		this.categories.clear();
		for(String key : arrayKeys) {
			// Depth
			if(orphans.contains(key)) {
				int computedDepth = computeDepth(key);
				if(computedDepth > this.depth)
					this.depth = computedDepth;
			}
			
			// Inferred subsumptions
			this.inferredSubsumptions += computeInferredSubsumptions(key);
		}
	}
	
	public void displayStatistics() {
		System.out.println("== DBPedia categories statistics ==");
		System.out.println("Classes number: " + this.categoriesNumber);
		System.out.println("Direct subsumptions number: " + this.directSubsumptions);
		System.out.println("Inferred subsumptions number: " + this.inferredSubsumptions);
		System.out.println("Orphans: " + this.orphansNumber);
		System.out.println("Depth: " + this.depth);
	}
	
	private int computeDepth(String key) {
		int computedDepth = 0;
		Stack<Pair<String, Integer>> stack = new Stack<>();
		stack.push(new Pair<String, Integer>(key, 0));
		
		try {
			while(!stack.isEmpty()) {
				Pair<String, Integer> pair = stack.pop();
				
				List<ChildAndParent> children = JSONReader.getChildrenAndParents(URLEncoder.encode(
		                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
		                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
		                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
		                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
		                + "select distinct ?child where "
		                + "{"
		                + "?child rdf:type skos:Concept . "
		                + "?child skos:broader ?parent . "
		                + "FILTER (REGEX(STR(?child), \"http://dbpedia.org/resource/Category\", \"i\")) . "
		                + "FILTER (REGEX(STR(?parent), \"" + pair.getValue1() + "\", \"i\")) ."
		                + "}", "UTF-8"));
				
				if(children.size() == 0 && pair.getValue2() > computedDepth)
					computedDepth = pair.getValue2();
				
				else {
					for(ChildAndParent child : children) 
						stack.push(new Pair<String, Integer>(child.getChild().getValue(), pair.getValue2() + 1));
				}
			}
		}
		
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private int computeInferredSubsumptions(String key) {
		int computedInferredSubsumptions = 0;
		Stack<String> stack = new Stack<String>();
		HashMap<String, Boolean> alreadySeen = new HashMap<String, Boolean>();
		
		List<ChildAndParent> parents = null;
		try {
			parents = JSONReader.getChildrenAndParents(URLEncoder.encode(
                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
                + "select distinct ?parent where "
                + "{"
                + "?parent rdf:type skos:Concept . "
                + "?child skos:broader ?parent . "
                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\")) . "
                + "FILTER (REGEX(STR(?child), \"" + key + "\", \"i\")) ."
                + "}", "UTF-8"));
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}
		
		for(ChildAndParent parent : parents) {
			stack.push(parent.getParent().getValue());
			alreadySeen.put(parent.getParent().getValue(), true);
		}
		
		while(!stack.isEmpty()) {
			String child = stack.pop();
			
			try {
				parents = JSONReader.getChildrenAndParents(URLEncoder.encode(
	                "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
	                + "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#> "
	                + "PREFIX owl:<http://www.w3.org/2002/07/owl#> "
	                + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#> "
	                + "select distinct ?parent where "
	                + "{"
	                + "?parent rdf:type skos:Concept . "
	                + "?child skos:broader ?parent . "
	                + "FILTER (REGEX(STR(?parent), \"http://dbpedia.org/resource/Category\", \"i\")) . "
	                + "FILTER (REGEX(STR(?child), \"" + child + "\", \"i\")) ."
	                + "}", "UTF-8"));
			}
			catch(UnsupportedEncodingException e) {
				e.printStackTrace();
				return -1;
			}
			
			for(ChildAndParent parent : parents) {
				if(alreadySeen.get(parent.getParent().getValue()) == null) {
					computedInferredSubsumptions++;
					alreadySeen.put(parent.getParent().getValue(), true);
					stack.push(parent.getParent().getValue());
				}
			}
		}
		
		return computedInferredSubsumptions;
	}
}
