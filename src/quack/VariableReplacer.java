package quack;

import java.util.HashMap;
import java.util.Map;

import apple.laf.JRSUIConstants.Size;

import com.sun.tools.javac.util.List;
public class VariableReplacer {
	
	public VariableReplacer() {
		// TODO Auto-generated constructor stub
	}
	
	public String alignVars(Map<String,String> originalVars, Map<String,String> replacementVars){
		// TODO: only returns one possible function call
		String match = "";
		if (replacementVars.get("return_type") != null){
			match += replacementVars.get("return_type") + " temp = ";
			replacementVars.remove("return_type");
		}
		if (replacementVars.get("method") != null){
			match += replacementVars.get("method") + "(";
			replacementVars.remove("method");
			int numInputParams = replacementVars.size();// we know all the remaining keys are input vars
			int replaced = 0;
				for(int i = 0; i<numInputParams; i++){
					for(String e : originalVars.keySet()){
					//System.out.println(originalVars.get(i));
					//System.out.println(replacementVars.get(e));
					if (originalVars.get(i) == replacementVars.get(e)){
						//System.out.println(replacementVars);
						//System.out.println(e);
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
			match = "";
		}
		//hacky to get around last variable comma
		
		//if (match != "")
			//System.out.println(numInputParams);
			//System.out.print(match);
		 
		return match;
	}

}
