package cl.gplaza.portero_rpi;

import java.util.HashMap;

public class RestQueryObject {

	private HashMap<String,String> params;
	private String url;
	
	public HashMap<String, String> getParams() {
		return params;
	}
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}	
}
