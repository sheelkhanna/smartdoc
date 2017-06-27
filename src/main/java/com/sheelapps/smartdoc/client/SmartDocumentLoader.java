package com.sheelapps.smartdoc.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.sheelapps.smartdoc.client.struct.Document;

public class SmartDocumentLoader {
	
	Document doc = null ;
	private Runnable callback;
	
	public void loadDocument(String path, Runnable runnable) {
		this.callback = runnable;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, path);
    try {
      requestBuilder.sendRequest(null, new RequestCallback() {

		public void onError(Request request, Throwable exception) {
			// TODO Auto-generated method stub
			Window.alert(exception.getMessage());
		}
		public void onResponseReceived(Request request, Response response) {
			// TODO Auto-generated method stub
			parseXMLText(response.getText());
		}
    	  
      });
    } catch (RequestException ex) {
    	Window.alert(ex.getMessage());
    }
		
}
	
	protected void parseXMLText(String xmlText) {
	 doc = SmartDocParser.parse(xmlText);	
	 if(callback!=null)
	       callback.run();
    
}

	

	public Document getDocument() {
		// TODO Auto-generated method stub
		return doc;
	}

	
}
