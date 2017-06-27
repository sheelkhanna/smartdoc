package com.sheelapps.gwt.gwtutils.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MessageDialog extends DialogBox {
		
	 public MessageDialog(String title,String text, boolean modal) {
		 super(true, modal);
	      // Set the dialog box's caption.
	      setText(title);

	      // DialogBox is a SimplePanel, so you have to set it's widget property to
	      // whatever you want its contents to be.
	    
	      VerticalPanel v = new VerticalPanel();
	      v.setSize("100%","100%");
	      final TextArea field = new TextArea(); 
	      field.setSize("100%","100%");
	      v.add(field);
	      field.setText(text);
	      v.setCellHeight(field, "98%");
	      
	      Button ok = new Button("Close");
	      v.add(ok);
	      v.setCellHorizontalAlignment(ok,HorizontalPanel.ALIGN_RIGHT);
	      
	      v.setSpacing(2);
	      
	      ok.addClickListener(new ClickListener() {
	        public void onClick(Widget sender) {
	          MessageDialog.this.hide();
	         
	        }
	      });
	      setWidget(v);
	      
	      //setTitle(title);
	      setSize("600px","600px");
	    }
		
		
		public boolean onKeyDownPreview(char key, int modifiers) {
		    // Use the popup's key preview hooks to close the dialog when either
		    // enter or escape is pressed.
		    switch (key) {
		      //case KeyboardListener.KEY_ENTER:
		      case KeyboardListener.KEY_ESCAPE:
		        hide();
		        break;
		    }

		    return true;
	   }
		
	 public static void showInformation(String title,String text, String style) {
		 MessageDialog dlg = new MessageDialog(title,text,false);
		 dlg.setStyleName(style);
		 dlg.center();
		 dlg.setAnimationEnabled(true);
		 dlg.show();
	 }
		
}
