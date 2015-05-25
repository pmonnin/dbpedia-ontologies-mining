package colibri.io.relation;

import java.io.FileReader;
import java.io.IOException;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import colibri.lib.Relation;
import colibri.lib.TreeRelation;


/**
 * Class for importing a binary relation from an .xml file.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class RelationReaderXML extends DefaultHandler {
	
	private String currentObject = null;
	private String currentAttribute = null;
	private Relation relation = null;
	
	
	public RelationReaderXML () {
		super();
	}

	
	/**
	 * reads an .xml file
	 * @param fileName the name of the file to be read
	 * @return a relation containing exactly those objects, attributes and pairs contained in the file
	 * @throws SAXException
	 * @throws IOException
	 */
	public Relation read(String fileName) throws SAXException, IOException {
		Relation relation = new TreeRelation();
		read(fileName, relation);
		return relation;
	}
	
	
	/**
	 * reads an .xml file
	 * @param fileName the name of the file to be read
	 * @param relation the relation to which the elements contained in the file will be added
	 */
	public void read(String fileName, Relation relation) throws SAXException, IOException {
		this.relation = relation;
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(this);
		reader.setErrorHandler(this);
		
		FileReader fileReader = new FileReader(fileName);
		try {
			reader.parse(new InputSource(fileReader));
		} finally {
			fileReader.close();
		}
	}
	
	
	public void startElement (String uri, String name, String qName, Attributes atts) {
		//if an object is read that has no ancestor that is an object add it to the relation
		if (qName.equals("object") && currentObject == null) {
			currentObject = atts.getValue("name");
			//if an attribute has already been read, the pair will be added
			//else attribute is null and only the object will be added
			relation.add(currentObject, currentAttribute);
		}
		//if an attribute is read that has no ancestor that is an attribute add it to the relation
		if (qName.equals("attribute") && currentAttribute == null) {
			currentAttribute = atts.getValue("name");
			//if an object has already been read, the pair will be added
			//else object is null and only the attribute will be added
			relation.add(currentObject, currentAttribute);
		}
	}
	
	
	public void endElement (String uri, String name, String qName) {
		if (qName.equals("object"))
			currentObject = null;
		if (qName.equals("attribute"))
			currentAttribute = null;
	}
}
