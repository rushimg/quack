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
					total++;
					ResponseObj temp_respObj = new ResponseObj();
					temp_respObj.setDisplayString(jsonObj.getJSONArray("items").getJSONObject(i).getJSONArray("answers").getJSONObject(j).get("answer_id").toString());
					temp_respObj.setReplacementString(this.cleanCode(jsonObj.getJSONArray("items").getJSONObject(i).getJSONArray("answers").getJSONObject(j).get("body").toString()));
					list.add(temp_respObj);
					//System.out.print(temp_respObj.getDisplayString());
					//System.out.print(temp_respObj.getReplacementString());
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	private String cleanCode(String input){
		String cleaned = Jsoup.parse(input).select("code").text();
		return cleaned;
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
	
	public String httpGetSO(URL url){
		 BufferedReader br;
		 StringBuilder builder = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
			String temp_line = null;
				//String rawText = null;
	 		 while ((temp_line = br.readLine()) != null)
	 		 {
	 			 builder.append(temp_line); 
	 		 }
			}
 		 catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		 return builder.toString();
	}
	
	public URL createURL(String quack){
		//TODO: need to tune this in
		//System.out.print(quack);
		String temp = quack.replaceAll(" ", "%20");
		//System.out.print(temp);
		String strUrl = "http://api.stackexchange.com/2.1/search?order=desc&sort=activity&tagged=java&intitle="
				+ temp
				//+ "&site=stackoverflow&filter=!--iqJbOieOg3";
				+ "&site=stackoverflow&filter=!)Rw3Mkmi9q6VhQg9UATQwQa(";
		//System.out.print(strUrl);
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return url;
	}
	
	public void junk(){
		//java.net.URLConnection connection = url.openConnection();
		 //java.io.InputStream is = connection.getInputStream();
		 //InputStreamReader isr = new InputStreamReader(is);
		 //BufferedReader br = new BufferedReader(isr);
        //URL url = sof.createURL(quack);
		 //System.out.print(url.toString());
        //System.out.print(sor.httpGetSO(url));
	     //JSONObject json = new JSONObject(rawText.toString());
		
		 //Object obj=JSONValue.parse(content.toString());
		//JSONArray finalResult=(JSONArray)obj;
		//System.out.println(finalResult);
	
		//System.out.print(jsonObj.getJSONArray("items").getJSONObject(0).getJSONArray("answers").getJSONObject(0).get("body"));
		//firstArray = jsonArray.getString(0);
		//jsonObj.
		//JSONArray jsonArray = jsonObj.getJSONArray("items");
		//JSONArray jsonArray2 = new JSONArray(jsonArray.toString());
		
		//JSONObject pilot = json.getJSONObject("pilot");
		//JSONObject jsonObj1 = new JSONObject(jsonArray2.get(1));
		//System.out.print(jsonObj);
		//temp_respObj.setDisplayString(jsonObj.getJSONArray("items").getJSONObject(1).getJSONArray("answers").getJSONObject(0).get("answer_id").toString());
		//temp_respObj.setReplacementString(jsonObj.getJSONArray("items").getJSONObject(1).getJSONArray("answers").getJSONObject(0).get("body").toString());
		//list.add(temp_respObj);		
	}	
	
	
}
