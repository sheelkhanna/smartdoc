package com.sheelapps.gwt.gwtutils.client;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class JSONCallback
{
   
    class JSONResponseHandler implements RequestCallback {

	   public void onError(Request request, Throwable exception) {
			// TODO Auto-generated method stub
		   defaultJSONHandler.handleRequestError(request, exception);
		}

		public void onResponseReceived(Request request, Response response) {
			if(response.getStatusCode()==200) {
				String responseText = response.getText();
			    JSONValue jsonValue = JSONParser.parse(responseText);
			    defaultJSONHandler.handleJSON(jsonValue);
			}
		}

	}
   
   class XMLResponseHandler implements RequestCallback {

	   public void onError(Request request, Throwable exception) {
			// TODO Auto-generated method stub
		   defaultXMLHandler.handleRequestError(request, exception);
		}

		public void onResponseReceived(Request request, Response response) {
			if(response.getStatusCode()==200) {
			  defaultXMLHandler.handleXML(response.getText());
			}
		}

	}
   
   public interface RPCHandler {
	 public void  handleRequestError(Request request, Throwable exception);
   }
   
   public interface JSONHandler extends RPCHandler {
	   public void handleJSON(JSONValue   json);
   }
   public interface XMLHandler extends RPCHandler {
	   public void handleXML(String xmlText);
   }
   
   protected HashMap scriptTags = new HashMap();
   protected HashMap callbacks = new HashMap();
   protected HashMap callbackHandler = new HashMap();
   protected int curIndex = 0;
   private JSONHandler defaultJSONHandler;
   private XMLHandler defaultXMLHandler;
  
   private native static void setup(JSONCallback h, String callback) /*-{
     window[callback]  = function(jsonData) {
       h.@com.sheelapps.gwt.gwtutils.client.JSONCallback::handle(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(jsonData,callback);
     }
   }-*/;

   private String reserveCallback(JSONHandler handler) {
     while (true) {
       if (!callbacks.containsKey(new Integer(curIndex))) {
         callbacks.put(new Integer(curIndex), null);
         String callbackName="__gwt_callback" + curIndex++;
         callbackHandler.put(callbackName,handler);
         return callbackName;
       }
     }
   }
  
   public native void addScript(String uniqueId, String url) /*-{
   var elem = document.createElement("script");
   elem.setAttribute("language", "JavaScript");
   elem.setAttribute("type", "text/javascript");
   elem.setAttribute("src", url);
   document.getElementsByTagName("body")[0].appendChild(elem);
 }-*/;
   
   public void loadJSONP(String url, JSONHandler handler) {
     String callbackName = reserveCallback(handler);
     setup(this, callbackName);
     addScript(callbackName, url + callbackName);
   }
   
   public void loadJSON(String url, JSONHandler handler) throws RequestException {
	   this.defaultJSONHandler = handler;
	   RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
       requestBuilder.sendRequest(null, new JSONResponseHandler());
		
   }
   
   public void loadXML(String url, XMLHandler handler) throws RequestException {
	   this.defaultXMLHandler = handler;
	   RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
       requestBuilder.sendRequest(null, new XMLResponseHandler());
		
   }
   
   public void postXML(String url, String xml, XMLHandler handler) throws RequestException {
	   this.defaultXMLHandler=handler;
	   StringBuffer postData = new StringBuffer();
	   postData.append("_xml").append("=").append(URL.encode(xml));
	   RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
	  // requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
	   requestBuilder.setHeader("Content-type", "text/xml");
       requestBuilder.sendRequest(postData.toString(), new XMLResponseHandler());
	  	   
	  
   }
   
  /**
   * JS callback method
   * @param jso
   * @param callback
   */
   public void handle(JavaScriptObject jso , String callback) {
     JSONObject json = new JSONObject(jso);
     ((JSONHandler)callbackHandler.get(callback)).handleJSON(json);
   }



}