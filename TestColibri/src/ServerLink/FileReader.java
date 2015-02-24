package ServerLink;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Temporary class
public class FileReader {

	private String nameFile;
	
	public FileReader(String nameFile)
	{
		this.nameFile = nameFile;
	}
	
	public String readFile() throws IOException
	{
		InputStream instream = new FileInputStream(nameFile); 
		InputStreamReader instreamReader = new InputStreamReader(instream);
		BufferedReader br = new BufferedReader(instreamReader);
		
		String line;
		String returnString = "";
		
		while ((line=br.readLine())!=null)
		{
			returnString+=line+"\n";
		}
		
		br.close();
		
		return returnString;
	}
	
	public void setNameFile(String nameFile)
	{
		this.nameFile = nameFile;
	}
}
