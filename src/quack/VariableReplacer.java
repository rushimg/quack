package quack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import apple.laf.JRSUIConstants.Size;

import com.sun.tools.javac.util.List;
public class VariableReplacer {
	Vector<String> permuteList;
	public VariableReplacer() {
		// TODO Auto-generated constructor stub
	}
	
	public Vector<String> alignVars(Map<String,String> originalVars, Map<String,String> replacementVars){
		Vector<String> matches = new Vector<String>();
		this.permuteList = new Vector<String>();
		String match = "";
		if (replacementVars.get("return_type") != null){
			match += replacementVars.get("return_type") + " temp = ";
			replacementVars.remove("return_type");
		}
		if (replacementVars.get("method") != null){
			match += replacementVars.get("method") + "(";
			
			replacementVars.remove("method");
			Vector<Vector<String>> permutedLists = this.runPermuter(originalVars);
			for(Vector<String> keycombos: permutedLists){
			String match2 = "";
			int numInputParams = replacementVars.size();// we know all the remaining keys are input vars
			String[] vars = new String[numInputParams];
			int replaced = 0;
				for(int i = 0; i<numInputParams; i++){
					for(String e : keycombos){
						//System.out.println(e);
					if (originalVars.get(i) == replacementVars.get(e)){
						//System.out.println(replacementVars.get(e));
						vars[i] = e;
						replacementVars.remove(e);
						i++;
						replaced++;
					}
			}

		}
		if (replaced == numInputParams){
			
			for(int j = 0; j<numInputParams; j++){
				match2 += vars[j] + ",";				
			}
				
				match2 += ");";
				match2 = match2.replace(",)", ")");
				match2 = match+match2;
		}
		else
			match2 = null;
		
		if (!matches.contains(match2))
			matches.add(match2); //no duplicates
			}
		}
		//System.out.println(matches);
		return matches;
	}
	
//	public Vector<Vector<String>> runPermuter(Map<String,String> originalVars){
//		Vector<Vector<String>> permutedMap = new Vector<Vector<String>>();
//		int size = originalVars.size();
//		String permuteThis = "";
//		for(Integer i = 0; i< size; i++){
//			permuteThis += i.toString();
//		}
//		Set<String> e =  originalVars.keySet();
//		String[] arr = new String[size];
//		arr = e.toArray(arr);
//		this.permute("",permuteThis);
//		for (String s : this.permuteList){
//			Vector<String> tempList = new Vector<String>();
//			for(Integer j = 0; j < size; j++){
//				Integer currentVal = Character.getNumericValue(s.charAt(j));
//				tempList.add(currentVal.toString());
//			}
//			permutedMap.add(tempList);
//		}
//		
//		return permutedMap;
//	}
//	
	public Vector<Vector<String>> runPermuter(Map<String,String> originalVars){
		Vector<Vector<String>> permutedMap = new Vector<Vector<String>>();
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
			Vector<String> tempList = new Vector<String>();
			for(Integer j = 0; j < size; j++){
				int currentVal = Character.getNumericValue(s.charAt(j));
				String Key = arr[currentVal];
				tempList.add(Key);
			}
			permutedMap.add(tempList);
		}
		
		return permutedMap;
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
