package quack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IRegion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.sun.tools.javac.util.List;

import MyUtil.UU;

public class SOFunctions {
	private int return_num = 10; 
	
	public SOFunctions() {
		// TODO Auto-generated constructor stub
	}
	
	public Vector<ResponseObj> processJSON(String rawText){
		Vector<ResponseObj> list = new Vector<ResponseObj>();
		int total  = 0;
		try {
			JSONObject jsonObj = new JSONObject(rawText.toString());
			for(int i = 0; i< this.return_num; i++){
				int num_answers = 0;
				
				try{num_answers= jsonObj.getJSONArray("items").getJSONObject(i).getInt("answer_count"); }
				catch (JSONException e) {
					break;
				}
				
				for (int j = 0; j<num_answers ; j++){
					if (total>=this.return_num){ break; }
					String repString  = this.cleanCode(jsonObj.getJSONArray("items").getJSONObject(i).getJSONArray("answers").getJSONObject(j).get("body").toString());
					if (repString != null){
					total++;
					ResponseObj temp_respObj = new ResponseObj();
					temp_respObj.setDisplayString(jsonObj.getJSONArray("items").getJSONObject(i).get("title").toString());
					//temp_respObj.setDisplayString(jsonObj.getJSONArray("items").getJSONObject(i).getJSONArray("answers").getJSONObject(j).get("answer_id").toString());
					temp_respObj.setReplacementString(repString);
					list.add(temp_respObj);
					}
				}
					
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private String cleanCode(String input){
		String cleaned = Jsoup.parse(input).select("code").text();
		if (cleaned.isEmpty()){ return null;} 
		else {return cleaned;}
	}
	
	public String httpGetSO(URL url){
		 BufferedReader br;
		 StringBuilder builder = new StringBuilder();
		
		try {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
			String temp_line = null;
	 		 while ((temp_line = br.readLine()) != null)
	 		 {
	 			 builder.append(temp_line); 
	 		 }
			}
 		 catch (IOException e) { e.printStackTrace(); }
 		 
		return builder.toString();
	}
	
	public URL createURL(String quack){
		String temp = quack.replaceAll(" ", "%20");
		String strUrl = "http://api.stackexchange.com/2.1/search?order=desc&sort=activity&tagged=java&intitle="
				+ temp
				//+ "&site=stackoverflow&filter=!--iqJbOieOg3";
				+ "&site=stackoverflow&filter=!)Rw3Mkmi9q6VhQg9UATQwQa(";
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) { e.printStackTrace();}
        
		return url;
	}	
	
	/*private JSONObject getSingleJSONObject(JSONObject jsonObj, int index){
		JSONObject temp = null;
		try {
		temp = jsonObj.getJSONArray("items").getJSONObject(index);
		} catch (JSONException e) { e.printStackTrace();} 
		return temp;	
		}*/

	/*private String getAnswerId(JSONObject jsonObj,int index1, int index2){
		return (jsonObj.getJSONArray("items").getJSONObject(index1).getJSONArray("answers").getJSONObject(index2).get("answer_id")).toString();
		}*/
}
