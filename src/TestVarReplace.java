import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import quack.VariableParser;
import quack.VariableReplacer;


public class TestVarReplace {

	@Test
	public void varReplacement3Strings() {
		VariableParser varPar = new VariableParser();
		//replacementString = "public String getOauthUrl(String clientId, String callbackUrl, String scope) { return \"test\";\" }";
		//varPar.parseSO(unit, repString)
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("String1","String");
		 originalVars.put("String2","String");
		 originalVars.put("String3","String");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","getOauthUrl");
		 tempMap.put("return_type","String");
		 tempMap.put("3","String");
		 tempMap.put("2","String");
		 tempMap.put("1","String");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("String temp = getOauthUrl(String3,String1,String2);", replaced);
	}

	@Test
	public void discardUselessTypeInfo() {
		VariableParser varPar = new VariableParser();
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("String1","public java.lang.String");
		 originalVars.put("String2","public java.lang.String");
		 originalVars.put("String3","public java.lang.String");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","getOauthUrl");
		 tempMap.put("return_type","String");
		 tempMap.put("3","String");
		 tempMap.put("2","String");
		 tempMap.put("1","String");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("String temp = getOauthUrl(String3,String1,String2);", replaced);
	}
	
	@Test
	public void varReplacement2Strings() {
		 VariableParser varPar = new VariableParser();
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("String1","public java.lang.String");
		 originalVars.put("String2","public java.lang.String");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","getOauthUrl");
		 tempMap.put("return_type","String");
		 tempMap.put("3","String");
		 tempMap.put("2","String");
		 tempMap.put("1","String");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("", replaced);
	}
	
	@Test
	public void varReplacementObject() {
		VariableParser varPar = new VariableParser();
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("rco","RandomComplexObject");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","doSomething");
		 tempMap.put("return_type","Map");
		 tempMap.put("1","RandomComplexObject");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("Map temp = doSomething(rco);", replaced);
	}
	
	
}
