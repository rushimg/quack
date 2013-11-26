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
		 assertEquals(null, replaced);
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
	
	@Test
	public void varReplacementObject2() {
		VariableParser varPar = new VariableParser();
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("br","BufferedReader");
		 originalVars.put("vc","Vector<String>");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","doSomethingAndReturnList");
		 tempMap.put("return_type","List<String>");
		 tempMap.put("1","Vector<String>");
		 tempMap.put("2","BufferedReader");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("List<String> temp = doSomethingAndReturnList(vc,br);", replaced);
	}
	
	
	@Test
	public void varReplacementNoFullNo() {
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("String1","String");
		 originalVars.put("String2","String");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","getOauthUrl");
		 tempMap.put("return_type","String");
		 tempMap.put("3","String");
		 tempMap.put("2","String");
		 tempMap.put("1","String");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals(null, replaced);
		 
		 Map<String, String> originalVars2 = new HashMap<String, String>();
		 originalVars2.put("String1","String");
		 originalVars2.put("String2","String");
		 originalVars2.put("String3","String");
		 Map<String, String>  tempMap2 = new HashMap<String, String>();
		 tempMap2.put("method","getOauthUrl");
		 tempMap2.put("return_type","String");
		 tempMap2.put("3","String");
		 tempMap2.put("2","String");
		 tempMap2.put("1","String");
		 VariableReplacer varRep2 = new VariableReplacer();
		 String replaced2 = varRep2.alignVars(originalVars2, tempMap2);
		 assertEquals("String temp = getOauthUrl(String3,String1,String2);", replaced2);
		 
		 Map<String, String> originalVars3 = new HashMap<String, String>();
		 originalVars3.put("String1","String");
		 originalVars3.put("String2","String");
		 Map<String, String>  tempMap3 = new HashMap<String, String>();
		 tempMap3.put("method","getOauthUrl");
		 tempMap3.put("return_type","String");
		 tempMap3.put("3","String");
		 tempMap3.put("2","String");
		 tempMap3.put("1","String");
		 VariableReplacer varRep3 = new VariableReplacer();
		 String replaced3 = varRep3.alignVars(originalVars3, tempMap3);
		 assertEquals(null, replaced3);
	}
	
	@Test
	public void varReplacementFullNoFull() {
		Map<String, String> originalVars2 = new HashMap<String, String>();
		 originalVars2.put("String1","String");
		 originalVars2.put("String2","String");
		 originalVars2.put("String3","String");
		 Map<String, String>  tempMap2 = new HashMap<String, String>();
		 tempMap2.put("method","getOauthUrl");
		 tempMap2.put("return_type","String");
		 tempMap2.put("3","String");
		 tempMap2.put("2","String");
		 tempMap2.put("1","String");
		 VariableReplacer varRep2 = new VariableReplacer();
		 String replaced2 = varRep2.alignVars(originalVars2, tempMap2);
		 assertEquals("String temp = getOauthUrl(String3,String1,String2);", replaced2);
		 
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("String1","String");
		 originalVars.put("String2","String");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","getOauthUrl");
		 tempMap.put("return_type","String");
		 tempMap.put("3","String");
		 tempMap.put("2","String");
		 tempMap.put("1","String");
		 VariableReplacer varRep = new VariableReplacer();
		 String replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals(null, replaced); 
		 
		 Map<String, String> originalVars3 = new HashMap<String, String>();
		 originalVars3.put("String3","String");
		 originalVars3.put("String1","String");
		 originalVars3.put("String2","String");
		 Map<String, String>  tempMap3 = new HashMap<String, String>();
		 tempMap3.put("method","getOauthUrl");
		 tempMap3.put("return_type","String");
		 tempMap3.put("3","String");
		 tempMap3.put("2","String");
		 tempMap3.put("1","String");
		 VariableReplacer varRep3 = new VariableReplacer();
		 String replaced3 = varRep3.alignVars(originalVars3, tempMap3);
		 assertEquals("String temp = getOauthUrl(String3,String1,String2);", replaced3);
	}

}
