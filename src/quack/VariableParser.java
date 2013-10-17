package quack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class VariableParser {
	
	public Map<String, String> varMap;
	
	public VariableParser() {
		// TODO Auto-generated constructor stub
	}

	public void parse(String inputCode){
		BufferedReader bufReader = new BufferedReader(new StringReader(inputCode));
		String line=null;
		try {
			while( (line=bufReader.readLine()) != null )
			{
				line = this.cleanString(line);
				//Simple Ones (primitives)
				/*if (line.indexOf("int") != -1)
					System.out.println(line);
				else if (line.indexOf("char") != -1)
					System.out.println(line);
				else*/
				this.getInstances(line);
				//More complex ones (objects)
			}
		} catch (IOException e) { e.printStackTrace();}
		
		//System.out.println(inputCode);
	}
	
	private String cleanString(String unCleanString){
		//repalce with space
		String cleanerUnCleanString = "";
		//cleanerUnCleanString = unCleanString.replace(")", " ");
		cleanerUnCleanString = unCleanString.replace(")", " ");
		cleanerUnCleanString = cleanerUnCleanString.replace("(", " ");
		cleanerUnCleanString = cleanerUnCleanString.replace("]"," ");
		cleanerUnCleanString = cleanerUnCleanString.replace("["," ");
		cleanerUnCleanString = cleanerUnCleanString.replace(","," ");
		//replace with nothing
		cleanerUnCleanString = cleanerUnCleanString.replace("\"","");
		cleanerUnCleanString = cleanerUnCleanString.replace("\'","");
		return cleanerUnCleanString;
	}
	
	private String getInstances(String line){
		// idea here is that java programs follow the typical format ClassName instanceName
		String[] spaces = line.split(" ");
		for(int i=0; i< spaces.length-1 ; i++){
			if ((spaces[i].length() > 0) && (spaces[i+1].length() > 0)){
				//TODO: add in all primitive types
				if (Character.isUpperCase(spaces[i].charAt(0)) || "int".equals(spaces[i])){
					//System.out.println("hjkasdkjas " + spaces[i] + " " + spaces[i+1]);
					if (Character.isLowerCase(spaces[i+1].charAt(0)))
						System.out.println(spaces[i] + " " + spaces[i+1]);
				}
			}
		}
		return "";
	}
	
	
}
