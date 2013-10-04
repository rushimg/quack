package quack;

public class ResponseObj {
	
	private String displayString = null;
	
	private String responseString = null;
	
	public ResponseObj() {
		
	}
	
	public void setDisplayString(String in){
		this.displayString = in;
	}
	
	public String getDisplayString(){
		return this.displayString;
	}
	
	public void setResponseString(String in){
		this.responseString = in;
	}
	
	public String getResponseString(){
		return this.responseString;
	}
	
}
