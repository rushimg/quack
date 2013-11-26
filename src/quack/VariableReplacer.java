package quack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import apple.laf.JRSUIConstants.Size;

import com.sun.tools.javac.util.List;
public class VariableReplacer {
	Vector<String> permuteList;
	public VariableReplacer() {
		// TODO Auto-generated constructor stub
	}
	
	public String alignVars(Map<String,String> originalVars, Map<String,String> replacementVars){
		this.permuteList = new Vector<String>();
		Vector<String> retList = new Vector<String>();
		//permute("","0123");
		String match = "";
		if (replacementVars.get("return_type") != null){
			match += replacementVars.get("return_type") + " temp = ";
			replacementVars.remove("return_type");
		}
		if (replacementVars.get("method") != null){
			match += replacementVars.get("method") + "(";
			replacementVars.remove("method");
			//this.runPermuter(originalVars);
			int numInputParams = replacementVars.size();// we know all the remaining keys are input vars
			int replaced = 0;
				for(int i = 0; i<numInputParams; i++){
					for(String e : originalVars.keySet()){
					if (originalVars.get(i) == replacementVars.get(e)){
						match += e + ",";
						replacementVars.remove(e);
						i++;
						replaced++;
					}
			}

		}
		if (replaced == numInputParams){
				match += ");";
			 	match = match.replace(",)", ")");
		}
		else
			match = null;
		}
		return match;
	}
	
	public Vector<Map<String, String>> permuted(Map<String,String> originalVars){
			Vector<Map<String, String>> permutedMap = new Vector<Map<String, String>>();
			//Map<String,String> permutedMap = new HashMap<String, String>();
			int size = originalVars.size();
			Set<String> keyList = originalVars.keySet(); 
			Set<String> permutedKeys = new HashSet<String>();
			while(permutedKeys.size() != size*size-1){
				Collections.shuffle((java.util.List<?>) keyList);
				//permutedKeys.add(keyList);
			}
			return permutedMap;
			
	}
	public Vector<Map<String, String>> runPermuter(Map<String,String> originalVars){
		Vector<Map<String, String>> permutedMap = new Vector<Map<String, String>>();
		int size = originalVars.size();
		String permuteThis = "";
		for(Integer i = 0; i< size; i++){
			permuteThis += i.toString();
		}
		Set<String> e =  originalVars.keySet();
		String[] arr = new String[size];
		arr = e.toArray(arr);
		this.permute("",permuteThis);
		for (String s : this.permuteList){
			Map<String, String> tempMap = new HashMap<String, String>();
			for(Integer j = 0; j < size; j++){
				int currentVal = Character.getNumericValue(s.charAt(j));
				String Key = arr[currentVal];
				System.out.println(Key);
				tempMap.put(Key, originalVars.get(Key));
			}
			permutedMap.add(tempMap);
			System.out.print(tempMap);
		}
		
		return null;
	}
	
	
	public void permute(String beginningString, String endingString) {
	    if (endingString.length() <= 1)
	    	this.permuteList.add(beginningString + endingString);
	    else
	      for (int i = 0; i < endingString.length(); i++) {
	        try {
	          String newString = endingString.substring(0, i) + endingString.substring(i + 1);
	          permute(beginningString + endingString.charAt(i), newString);
	        } catch (StringIndexOutOfBoundsException exception) {
	          exception.printStackTrace();
	        }
	      }
	  }
	
}
