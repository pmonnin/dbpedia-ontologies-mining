package colibri.app;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


import org.xml.sax.SAXException;

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
public class FCA {
	
	public static void main (String[] args) {
		LongOpt[] longopts = new LongOpt[16];
		longopts[0] = new LongOpt("input_format", LongOpt.REQUIRED_ARGUMENT, null, 0);
		longopts[1] = new LongOpt("input_file", LongOpt.REQUIRED_ARGUMENT, null, 1);
		longopts[2] = new LongOpt("output_format", LongOpt.REQUIRED_ARGUMENT, null, 2);
		longopts[3] = new LongOpt("output_file", LongOpt.REQUIRED_ARGUMENT, null, 3);
		longopts[4] = new LongOpt("overwrite", LongOpt.NO_ARGUMENT, null, 4);
		longopts[5] = new LongOpt("traversal", LongOpt.REQUIRED_ARGUMENT, null, 5);
		longopts[6] = new LongOpt("rtype", LongOpt.REQUIRED_ARGUMENT, null, 6);
		longopts[7] = new LongOpt("ltype", LongOpt.REQUIRED_ARGUMENT, null, 7);
		longopts[8] = new LongOpt("supp", LongOpt.REQUIRED_ARGUMENT, null, 8);
		longopts[9] = new LongOpt("conf", LongOpt.REQUIRED_ARGUMENT, null, 9);
		longopts[10] = new LongOpt("diff", LongOpt.REQUIRED_ARGUMENT, null, 10);
		longopts[11] = new LongOpt("informat", LongOpt.REQUIRED_ARGUMENT, null, 0);
		longopts[12] = new LongOpt("infile", LongOpt.REQUIRED_ARGUMENT, null, 1);
		longopts[13] = new LongOpt("outformat", LongOpt.REQUIRED_ARGUMENT, null, 2);
		longopts[14] = new LongOpt("outfile", LongOpt.REQUIRED_ARGUMENT, null, 3);
		longopts[15] = new LongOpt("trav", LongOpt.REQUIRED_ARGUMENT, null, 5);
		
		Getopt getopt = new Getopt("FCA", args, "", longopts);
		
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

		int g = -1;
		while ((g = getopt.getopt()) != -1) {
			switch(g) {
			case 0:
				inputFormat = getopt.getOptarg();
				break;
			case 1:
				inputFile = getopt.getOptarg();
				break;
			case 2:
				outputFormat = getopt.getOptarg();
				break;
			case 3:
				outputFile = getopt.getOptarg();
				break;
			case 4:
				overwrite = "yes";
				break;
			case 5:
				trav = getopt.getOptarg();
				break;
			case 6:
				relationType = getopt.getOptarg();
				break;
			case 7:
				latticeType = getopt.getOptarg();
				break;
			case 8:
				supp = Integer.valueOf(getopt.getOptarg());
				break;
			case 9:
				conf = Float.valueOf(getopt.getOptarg());
				break;
			case 10:
				diff = Integer.valueOf(getopt.getOptarg());
				break;
			default:
				System.out.println("WARNING: unknown option was skipped.");
			}
		}
		
		
		
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
			else {
				System.out.println("Traversal \"" + trav + "\" is not known.");
				System.out.println("Please choose one of the following traversals:");
				System.out.println("bo  - bottom up, breadth first, by object set");
				System.out.println("bos - bottom up, breadth first, by object set size");
				System.out.println("ba  - bottom up, breadth first, by attribute set");
				System.out.println("bas - bottom up, breadth first, by attribute set size");
				System.out.println("to  - top down, breadth first, by object set");
				System.out.println("tos - top down, breadth first, by object set size");
				System.out.println("ta  - top down, breadth first, by attribute set");
				System.out.println("tas - top down, breadth first, by attribute set size");
				System.out.println("tdf - top down, depth first");
				System.out.println("bdf - top down, depth first");
				return;
			}
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
			else {
				System.out.println("Relation type \"" + relationType + "\" is not known.");
				System.out.println("Please choose one of the following types of relation:");
				System.out.println("hash");
				System.out.println("tree");
				return;
			}
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
			System.out.println("Input format \"" + inputFormat + "\" is not known.");
			System.out.println("Supported input formats include:");
			System.out.println("con");
			System.out.println("xml");
			return;
		}
		
		Lattice lattice;
		
		if (latticeType != null) {
			if (latticeType.equals("set"))
				lattice = new RawLattice(relation);
			else if (latticeType.equals("raw"))
				lattice = new RawLattice(relation);
			else if (latticeType.equals("hybrid"))
				lattice = new HybridLattice(relation);
			else {
				System.out.println("Lattice type \"" + latticeType + "\" is not known.");
				System.out.println("Please choose one of the following types of lattices:");
				System.out.println("raw");
				System.out.println("hybrid");
				return;
			}
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
