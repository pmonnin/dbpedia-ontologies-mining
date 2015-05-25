package colibri.io.relation;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.SAXException;

import colibri.lib.Relation;
import colibri.lib.TreeRelation;



/**
 * Class for importing a binary relation from a .con file.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class RelationReaderCON  {
	
	public Relation read(String fileName) throws SAXException, IOException {
		Relation relation = new TreeRelation();
		read(fileName, relation);
		return relation;
	}
	
	
	/**
	 * reads a .con file
	 * @param fileName the name of the file to be read
	 * @return a relation containing those pairs specified in the file
	 * @throws IOException
	 */
	public Relation read(String fileName, Relation relation) throws IOException {
		String currentObject = null;
		String currentAttribute = null;
		StringBuffer buffer = new StringBuffer();
		boolean comment = false;
		
		FileReader reader = new FileReader(fileName);
		
		try {
			char[] nextChar = {' '};
			
			while (reader.read(nextChar) != -1) {
				if (!comment) {
					if (currentObject == null) {
						switch (nextChar[0]) {
						case ' ':
						case '\t':
						case '\n':
							//clear the buffer
							buffer = new StringBuffer();
							break;
						case '%':		//comment
							
							buffer = new StringBuffer();
							comment = true;
							break;
						case ':':		//end of object
							currentObject = buffer.toString();
							relation.add(currentObject, null);
							buffer = new StringBuffer();
							break;
						default:		//read the next character into the buffer
							buffer.append(nextChar[0]);
						}
					}
					else {
						switch (nextChar[0]) {
						case ' ':
						case '\t':
						case '\n':		//end of attribute
							if (buffer.length() > 0) {
								currentAttribute = buffer.toString();
								relation.add(currentObject, currentAttribute);
								buffer = new StringBuffer();
								currentAttribute = null;
							}
							break;
						case '%':		//comment
							buffer = new StringBuffer();
							comment = true;
							break;
						case ';':		//end of attribute list
							if (buffer.length() > 0) {
								currentAttribute = buffer.toString();
								relation.add(currentObject, currentAttribute);
								buffer = new StringBuffer();
								currentAttribute = null;
							}
							currentObject = null;
							break;
						default:		//read the next character into the buffer
							buffer.append(nextChar[0]);
						}
					}
				}
				else {
					if (nextChar[0] == '\n')
						comment = false;
				}
			}
			return relation;
		}
		finally {
			reader.close();
		}
	}
}