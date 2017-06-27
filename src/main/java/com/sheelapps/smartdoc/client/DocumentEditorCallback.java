package com.sheelapps.smartdoc.client;

import com.google.gwt.user.client.ui.RootPanel;

public class DocumentEditorCallback implements Runnable {

	private RootPanel rootPanel;
	private SmartDocumentLoader loader;
	private String style;
	
	public DocumentEditorCallback(RootPanel rootPanel,
			SmartDocumentLoader loader, String style) {
		this.rootPanel = rootPanel;
		this.loader = loader;
		this.style=style;
	}

	public void run() {
		// TODO Auto-generated method stub
		rootPanel.add(new DocumentEditor(loader.getDocument(),true,style));
	}

}
