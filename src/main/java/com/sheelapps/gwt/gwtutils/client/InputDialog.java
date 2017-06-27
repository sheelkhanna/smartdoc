package com.sheelapps.gwt.gwtutils.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InputDialog extends DialogBox {
	
	
	 private String value=null;
	 private Runnable callback;

	 public InputDialog(String title, String defaultValue) {
		 
	      // Set the dialog box's caption.
	      setText(title);

	      // DialogBox is a SimplePanel, so you have to set it's widget property to
	      // whatever you want its contents to be.
	      Button ok = new Button("OK");
	      Button cancel = new Button("Cancel");
	      final TextBox field = new TextBox(); 
	      if(defaultValue!=null)
	    	 field.setText(defaultValue);
	      field.setWidth("100%");
	      VerticalPanel v = new VerticalPanel();
	      v.add(field);
	      HorizontalPanel h = new HorizontalPanel();
	      h.add(ok);
	      h.add(cancel);
	      h.setSpacing(5);
	      
	      v.add(h);
	      
	      ok.addClickListener(new ClickListener() {
	        public void onClick(Widget sender) {
	          value =	field.getText();
	          InputDialog.this.hide();
	          if(callback!=null)
	        	  callback.run();
	        }
	      });
	      cancel.addClickListener(new ClickListener() {
		        public void onClick(Widget sender) {
		          value =	null;
		          InputDialog.this.hide();
		          if(callback!=null)
		        	  callback.run();
		        }
		      });
	      v.setSpacing(5);
	      setWidget(v);
	      
	    }
		
		public String getValue() {
			return value;
		}
		public void setCallback(Runnable callback) {
			this.callback=callback;
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
}
