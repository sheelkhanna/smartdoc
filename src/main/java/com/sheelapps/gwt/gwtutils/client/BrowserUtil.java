package com.sheelapps.gwt.gwtutils.client;

import java.util.HashMap;

import com.google.gwt.http.client.URL;

/**
 * 
 * @author Sheel Khanna
 *
 */
public class BrowserUtil {

	
	/**
	   * Get the URL of the page, without an hash of query string.
	   * 
	   * @return the location of the page
	   */
	  public static native String getHostPageLocation() /*-{
	    var s = $doc.location.href;
	  
	    // Pull off any hash.
	    var i = s.indexOf('#');
	    if (i != -1)
	      s = s.substring(0, i);
	  
	    // Pull off any query string.
	    i = s.indexOf('?');
	    if (i != -1)
	      s = s.substring(0, i);
	  
	    // Ensure a final slash if non-empty.
	    return s;
	  }-*/;
	
	public static native String getParamString() /*-{

	   return $wnd.location.search;

	}-*/;
	
	public static native String getHistoryString() /*-{
	 var s = $doc.location.href;
     var i = s.indexOf('#');
	 if (i != -1)
	    s = s.substring(i+1);
	// Pull off any query string.
	    i = s.indexOf('?');
	    if (i != -1)
	      s = s.substring(0, i);
	  
	    // Ensure a final slash if non-empty.
	    return s;
	
	}-*/;

	public static HashMap<String, String> parseParamString() {
		// String with ? as starting text.
		String string = getParamString();
		HashMap<String, String> map = new HashMap<String, String>();
		if(string!=null && string.length() > 1 && string.indexOf("&")> 1) {
			String[] ray = string.substring(1, string.length()).split("&");
			for (int i = 0; i < ray.length; i++) {
				String[] substrRay = ray[i].split("=");
				map.put(substrRay[0], URL.decode(substrRay[1]));
			}
		}else {
			if(string!=null && string.indexOf("=") > 1) {
				String[] substrRay = string.substring(1, string.length()).split("=");
				map.put(substrRay[0], URL.decode(substrRay[1]));
			}
		}
		return map;
	}
	
	public static HashMap<String, String> parseHistoryToken(String historyToken) {
		HashMap<String, String> map = new HashMap<String, String>();
		if(historyToken!=null && historyToken.length() > 1 && historyToken.indexOf("&")> 1) {
			String[] ray = historyToken.substring(0, historyToken.length()).split("&");
			for (int i = 0; i < ray.length; i++) {
				String[] substrRay = ray[i].split("=");
				map.put(substrRay[0], URL.decode(substrRay[1]));
			}
		}else {
			if(historyToken!=null && historyToken.indexOf("=") > 0) {
				String[] substrRay = historyToken.substring(0, historyToken.length()).split("=");
				map.put(substrRay[0], URL.decode(substrRay[1]));
			}
		}
		return map;
	}
	
}
