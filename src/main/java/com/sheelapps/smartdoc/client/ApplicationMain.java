package com.sheelapps.smartdoc.client;

import java.util.HashMap;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.sheelapps.smartdoc.client.struct.Document;
import com.sheelapps.smartdoc.client.struct.DocumentTab;
import com.sheelapps.smartdoc.client.struct.DocumentTreeItem;
/**
 * SmartDoc is an outline based document creation and publishing tool.
It is designed make structured, understandable documents easily and quickly.
SmartDoc has been found useful for story writing, FAQ creation, novel planning, 
speech-writing, manual writing, software support, biographies, and lesson planning. 
 * @author Sheel
 *
 */
public class ApplicationMain implements EntryPoint {

	static HashMap<String,String> paramMap = null;
	private static boolean historySupport = true; 
	public void onModuleLoad() {
		exportLoadFunction();
		History.addHistoryListener(new SmartDocHistoryListener());
		if(DOM.getElementById("_smloading")!=null)
			DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("_smloading"));
		callLoadFunction();
	}
	
	public static native void callLoadFunction() /*-{
	  if(typeof($wnd._onSmartDocLoad) != 'undefined')
	  	$wnd._onSmartDocLoad();
	}-*/;
	
	
	public static void loadSmartDoc(String url, String container, String mode, String style) {
		RootPanel rootPanel = null;
		if(style==null) style="";
		if(url!=null) {
		 if(container!=null && container.trim().length() > 0) {
				rootPanel = RootPanel.get(container);
			}else {
				rootPanel = RootPanel.get();
			}
		  if(rootPanel!=null) {
			if(mode!=null && mode.equalsIgnoreCase(SmartDocConstants.EDIT_MODE)) {
				//edit mode
				final SmartDocumentLoader loader = new SmartDocumentLoader();
				loader.loadDocument(url, new DocumentEditorCallback(rootPanel,loader,style));
			}else {
				//view mode
				final SmartDocumentLoader loader = new SmartDocumentLoader();
				loader.loadDocument(url, new DocumentViewerCallback(rootPanel,loader,style));
			 }
		  }
		}
	}
	
	public static void newSmartDoc(String container,String style) {
		RootPanel rootPanel = null;
		if(style==null) style="";
		if(container!=null && container.trim().length() > 0) {
				rootPanel = RootPanel.get(container);
			}else {
				rootPanel = RootPanel.get();
			}
			if(rootPanel!=null)
			   rootPanel.add(new DocumentEditor(blankDocument(),true,style));
			
		}
	
	public static Document blankDocument() {
		Document doc = new Document(SmartDocConstants.NEW_DOCUMENT_NAME);
		doc.setTitle(SmartDocConstants.NEW_DOCUMENT_TITLE);
		DocumentTab tab = new DocumentTab(SmartDocConstants.NEW_TAB_TITLE);
		DocumentTreeItem node = new DocumentTreeItem(SmartDocConstants.TITLE_DEFAULT_NODE);
		node.setData(SmartDocConstants.TEXT_DEFAULT);
		tab.addTreeItem(node);
		doc.addTab(tab);
		return doc;
	}
	
	public static void disableHistorySupport() {
		historySupport = false;
	}
	
	public static native void exportLoadFunction() /*-{
	$wnd.SmartDoc = {} ;
    $wnd.SmartDoc.load = function (url, container, mode,style) {
       @com.sheelapps.smartdoc.client.ApplicationMain::loadSmartDoc(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(url,container,mode,style);
       };
       
     $wnd.SmartDoc.newDocument = function (container,style) {
       @com.sheelapps.smartdoc.client.ApplicationMain::newSmartDoc(Ljava/lang/String;Ljava/lang/String;)(container,style);
       };   
   
     $wnd.SmartDoc.disableHistorySupport = function() {
       @com.sheelapps.smartdoc.client.ApplicationMain::disableHistorySupport()();
     };
   }-*/;

	public static boolean isHistorySupport() {
		// TODO Auto-generated method stub
		return historySupport;
	}
	

}
