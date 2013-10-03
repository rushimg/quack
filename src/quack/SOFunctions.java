package quack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.text.IRegion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import MyUtil.UU;

public class SOFunctions {
	
	public SOFunctions() {
		// TODO Auto-generated constructor stub
	}
	
	public String processJSON(String rawText){
		String firstArray = null;
		
		try {
			JSONObject jsonObj = new JSONObject(rawText.toString());
			
			//JSONArray jsonArray = new JSONArray(rawText.toString());
			System.out.print(jsonObj);
			//firstArray = jsonArray.getString(0);
			//jsonObj.
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rawText;
	}
	
	public String httpGetSO(URL url){
		 BufferedReader br;
		 StringBuilder builder = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(new GZIPInputStream(url.openStream())));
			String temp_line = null;
				String rawText = null;
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
		String temp = quack.replaceAll(" ", "%20");
		String strUrl = "http://api.stackexchange.com/2.1/search?order=desc&sort=activity&tagged=java&intitle="
				+ temp
				+ "&site=stackoverflow&filter=!--iqJbOieOg3";
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
	}	
	
	
}
