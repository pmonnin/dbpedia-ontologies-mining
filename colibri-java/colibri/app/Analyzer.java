package colibri.app;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


import org.xml.sax.SAXException;

import colibri.io.lattice.ConceptWriterString;
import colibri.io.lattice.LatticeWriterDot;
import colibri.io.lattice.LatticeWriterXmlEdges;
import colibri.io.relation.RelationReaderCON;
import colibri.io.relation.RelationReaderXML;
import colibri.lib.Concept;
import colibri.lib.Edge;
import colibri.lib.HashRelation;
import colibri.lib.HybridLattice;
import colibri.lib.Lattice;
import colibri.lib.RawLattice;
import colibri.lib.Relation;
import colibri.lib.Traversal;
import colibri.lib.TreeRelation;


/**
 * Imports a binary relation from a .con or .xml file and
 * outputs the edges of the corresponding lattice or
 * the edges returned by the violation iterator.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class Analyzer {
	
	public static void main (String[] args) {
		String inputFormat = null;
		String inputFile = null;
		String outputFormat = null;
		String outputFile = null;
		String overwrite = null;
		
		String trav = null;
		String relationType = null;
		String latticeType = null;
		
		int supp = 20;
		float conf = 0.9f;
		int diff = 2;

		inputFormat = System.getProperty("input_format");
		inputFile = System.getProperty("input_file");
		outputFormat = System.getProperty("output_format");
		outputFile = System.getProperty("output_file");
		overwrite = System.getProperty("overwrite");
		
		trav = System.getProperty("traversal");
		relationType = System.getProperty("rtype");
		latticeType = System.getProperty("ltype");
		
		if (System.getProperty("supp") != null)
			supp = Integer.valueOf(System.getProperty("supp"));

		if (System.getProperty("conf") != null)
			conf = Float.valueOf(System.getProperty("conf"));

		if (System.getProperty("diff") != null)
			diff = Integer.valueOf(System.getProperty("diff"));
		
		if (inputFile == null) {
			System.err.println("Please specify the file name of the input file.");
			return;
		}
		
		if (inputFormat == null) {
			if (inputFile.endsWith("xml")) {
				inputFormat = "xml";
			}
			else if (inputFile.endsWith("con")) {
				inputFormat = "con";
			}
			else {
				System.err.println("Please specify the format of the input file.");
				return;
			}
			
		}
		
		if (outputFormat == null) {
			outputFormat = "xml";
		}
		
		if (outputFile == null && !outputFormat.equals("size") && !outputFormat.equals("vio")) {
			System.err.println("Please specify the file name of the output file.");
			return;
		}
		
		Traversal traversal;
		
		if (trav != null) {
			if (trav.equals("bo"))
				traversal = Traversal.BOTTOM_OBJ;
			else if (trav.equals("bos"))
				traversal = Traversal.BOTTOM_OBJSIZE;
			else if (trav.equals("ba"))
				traversal = Traversal.BOTTOM_ATTR;
			else if (trav.equals("bas"))
				traversal = Traversal.BOTTOM_ATTRSIZE;
			else if (trav.equals("to"))
				traversal = Traversal.TOP_OBJ;
			else if (trav.equals("tos"))
				traversal = Traversal.TOP_OBJSIZE;
			else if (trav.equals("ta"))
				traversal = Traversal.TOP_ATTR;
			else if (trav.equals("tas"))
				traversal = Traversal.TOP_ATTRSIZE;
			else if (trav.equals("tdf"))
				traversal = Traversal.TOP_DEPTHFIRST;
			else if (trav.equals("bdf"))
				traversal = Traversal.BOTTOM_DEPTHFIRST;
			else
				throw new IllegalArgumentException("Traversal \"" + trav + "\" is not known");
		}
		else {
			traversal = Traversal.TOP_ATTR;
		}
		
		File file = null;
		
		if (!outputFormat.equals("size") && !outputFormat.equals("vio")) {
			file = new File(outputFile);
			
			if (file.exists()) {
				if (overwrite == null || !overwrite.equals("yes") || !file.canWrite()) {
					System.err.println("Unable to write to the specified file. The file already exists.");
					return;
				}
				else {
					try {
						file.createNewFile();
					} catch (IOException e) {
						System.err.println("Unable to write to the specified file.");
						e.printStackTrace();
						return;
					}
				}
			}
			else {
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.err.println("Unable to write to the specified file.");
					e.printStackTrace();
					return;
				}			
			}
			
			if (!file.canWrite()) {
				System.err.println("Unable to write to the specified file.");
				return;
			}
		}
		
		Relation relation;
		
		if (relationType != null) {
			if (relationType.equals("tree"))
				relation = new TreeRelation();
			else if (relationType.equals("hash"))
				relation = new HashRelation();
			else
				throw new IllegalArgumentException();
		}
		else {
			relation = new TreeRelation();
		}
		
		if (inputFormat.equals("xml")) {
				try {
					RelationReaderXML xmlReader = new RelationReaderXML();
					xmlReader.read(inputFile, relation);
				} catch (SAXException e) {
					System.err.println("Reading xml-file failed.");
					e.printStackTrace();
					return;
				} catch (IOException e) {
					System.err.println("Reading xml-file failed.");
					e.printStackTrace();
					return;
				}
		}
		else if (inputFormat.equals("con")) {
			try {
				RelationReaderCON conReader = new RelationReaderCON();
				conReader.read(inputFile, relation);
			} catch (IOException e) {
				System.err.println("Reading con-file failed.");
				e.printStackTrace();
			}
		}
		else {
			throw new IllegalArgumentException();
		}
		
		Lattice lattice;
		
		if (latticeType != null) {
			if (latticeType.equals("set"))
				lattice = new RawLattice(relation);
			else if (latticeType.equals("hybrid"))
				lattice = new HybridLattice(relation);
			else
				throw new IllegalArgumentException();
		}
		else {
			lattice = new HybridLattice(relation);
		}
		
		System.out.println(relation.getSizeAttributes() + " attributes");
		System.out.println(relation.getSizeObjects() + " objects");
		
		try {
			if (outputFormat.equals("dot")) {
				LatticeWriterDot writer = new LatticeWriterDot();
				writer.write(lattice, file, "; ", ", ", traversal);
				
				System.out.println("Output written to " + outputFile);
			}
			else if (outputFormat.equals("size")) {
				
				int i = 0;
				for (Iterator<Concept> it = lattice.conceptIterator(traversal); it.hasNext(); it.next()) {
					i++;
				}
				
				System.out.println("number of objects:    " + relation.getSizeObjects());
				System.out.println("number of attributes: " + relation.getSizeAttributes());
				System.out.println("number of concepts:   " + i);
			}
			else if (outputFormat.equals("vio")) {
				Iterator<Edge> it = lattice.violationIterator(supp, conf, diff);
				
				while (it.hasNext()) {
					Edge violation = it.next();
					int upperObjects = violation.getUpper().getObjects().size();
					int lowerObjects = violation.getLower().getObjects().size();
					int upperAttributes = violation.getUpper().getAttributes().size();
					int lowerAttributes = violation.getLower().getAttributes().size();
					System.out.println("violation (confidence 0." + lowerObjects * 100 / upperObjects + " support  " + lowerObjects +")");
					System.out.println("  " + violation);
					System.out.println("uo: " + upperObjects + " ua: " + upperAttributes + " lo: " + lowerObjects + " la: " + lowerAttributes);
				}
			}
			else if (outputFormat.equals("constring")) {
				ConceptWriterString writer = new ConceptWriterString();
				writer.write(lattice, file, traversal);				
			}
			else {
				LatticeWriterXmlEdges writer = new LatticeWriterXmlEdges();
				writer.write(lattice, file, traversal);
				
				System.out.println("Output written to " + outputFile);
			}
		} catch (IOException e) {
			System.err.println("Writing dot-file failed.");
			e.printStackTrace();
			return;
		}
		
	}
}
