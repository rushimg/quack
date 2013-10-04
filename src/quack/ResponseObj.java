package quack;

public class ResponseObj {
	
	private String displayString = null;
	
	private String replacementString = null;
	
	public ResponseObj() {
		
	}
	
	public void setDisplayString(String in){
		this.displayString = in;
	}
	
	public String getDisplayString(){
		return this.displayString;
	}
	
	public void setReplacementString(String in){
		this.replacementString = in;
	}
	
	public String getReplacementString(){
		return this.replacementString;
	}
	
}
