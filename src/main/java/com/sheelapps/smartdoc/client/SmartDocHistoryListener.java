package com.sheelapps.smartdoc.client;

import java.util.HashMap;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.sheelapps.gwt.gwtutils.client.BrowserUtil;

public class SmartDocHistoryListener implements HistoryListener {

	static HashMap<String, SmartDocCommand> docMap = new HashMap<String,SmartDocCommand>(); 
	
	public void onHistoryChanged(String historyToken) {
		if(!ApplicationMain.isHistorySupport()) return;
		try {
			HashMap<String, String> map = BrowserUtil.parseHistoryToken(historyToken);
			String tabTitle = map.get(SmartDocConstants.TAB_TITLE_QS);
			SmartDocCommand viewer = docMap.get(tabTitle);
			if(viewer!=null) {
			  viewer.selectTab(tabTitle,true);
			  String nodeTitle = map.get(SmartDocConstants.NODE_TITLE_QS);
			  if(nodeTitle!=null) {
				  viewer.selectNode(nodeTitle,true);
			  }
			}
		}catch(Exception e) {
			
		}
		
	}

	public static void newItem(SmartDocCommand documentViewer, String tabTitle, String nodeTitle) {
		if(!ApplicationMain.isHistorySupport()) return;
		String link = SmartDocConstants.TAB_TITLE_QS+"="+tabTitle;	
		if(nodeTitle!=null)
			link += "&"+SmartDocConstants.NODE_TITLE_QS+"="+nodeTitle;	
		History.newItem(link);
		docMap.put(tabTitle,documentViewer);
		
	}

}
