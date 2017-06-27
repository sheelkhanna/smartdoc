package com.sheelapps.smartdoc.client;

import com.google.gwt.user.client.ui.DialogBox;

public class PopupViewer extends DialogBox {

	public PopupViewer(DocumentViewer viewer) {
		 super(true);
		 setWidget(viewer);
		 setSize("600px","600px");
		 viewer.setSize("100%", "100%");
	      //setTitle(title);
	      
	}
	
	public static void showDocument (DocumentViewer d) {
		PopupViewer dlg = new PopupViewer(d);
		dlg.setAnimationEnabled(true);
		 dlg.setSize("600px","600px");
		 dlg.center();
		 dlg.show();
		 
	}
}
