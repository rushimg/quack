import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains("String temp = getOauthUrl(String3,String1,String2);"));
		 assertTrue(replaced.contains("String temp = getOauthUrl(String3,String2,String1);"));
		 assertTrue(replaced.contains("String temp = getOauthUrl(String2,String1,String3);"));
		 assertTrue(replaced.contains("String temp = getOauthUrl(String2,String3,String1);"));
		 assertTrue(replaced.contains("String temp = getOauthUrl(String1,String2,String3);"));
		 assertTrue(replaced.contains("String temp = getOauthUrl(String1,String3,String2);"));
		 assertEquals(6, replaced.size());
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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains("String temp = getOauthUrl(String3,String1,String2);"));
		 assertEquals(6, replaced.size());
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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains(null));
		 assertEquals(1, replaced.size());
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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertEquals("Map temp = doSomething(rco);", replaced.get(0));
		 assertEquals(1, replaced.size());
	}
	
	@Test
	public void varReplacementCorrectOrder() {
		VariableParser varPar = new VariableParser();
		 Map<String, String> originalVars = new HashMap<String, String>();
		 originalVars.put("br","BufferedReader");
		 originalVars.put("vc","Vector<String>");
		 Map<String, String>  tempMap = new HashMap<String, String>();
		 tempMap.put("method","doSomethingAndReturnList");
		 tempMap.put("return_type","List<String>");
		 tempMap.put("0","Vector<String>");
		 tempMap.put("1","BufferedReader");
		 VariableReplacer varRep = new VariableReplacer();
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains("List<String> temp = doSomethingAndReturnList(vc,br);"));
		 //System.out.println(replaced.get(0));
		 //System.out.println(replaced.get(1));
		 assertEquals(1, replaced.size());
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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains(null));
		 assertEquals(1, replaced.size());
		 
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
		 Vector<String> replaced2 = varRep2.alignVars(originalVars2, tempMap2);
		 assertTrue(replaced2.contains("String temp = getOauthUrl(String3,String1,String2);"));
		 assertFalse(replaced2.contains(null));
		 assertEquals(6, replaced2.size());
		 
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
		 Vector<String> replaced3 = varRep3.alignVars(originalVars3, tempMap3);
		 assertTrue(replaced3.contains(null));
		 assertEquals(1, replaced3.size());
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
		 Vector<String> replaced2 = varRep2.alignVars(originalVars2, tempMap2);
		 assertTrue(replaced2.contains("String temp = getOauthUrl(String3,String1,String2);"));
		 assertEquals(6, replaced2.size());
		 
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
		 Vector<String> replaced = varRep.alignVars(originalVars, tempMap);
		 assertTrue(replaced.contains(null));
		 assertEquals(1, replaced.size());
		 
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
		 Vector<String> replaced3 = varRep3.alignVars(originalVars3, tempMap3);
		 assertTrue(replaced3.contains("String temp = getOauthUrl(String3,String1,String2);"));
		 assertEquals(6, replaced3.size());
		 
	}

}
