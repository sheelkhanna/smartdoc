package com.sheelapps.gwt.gwtutils.client.richtext;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RichText extends Composite {

	private RichTextArea textArea;
	private RichTextToolbar toolbar;
	private HorizontalPanel tpanel;
	private StackPanel stackPanel;
	private HTML html;
	private String DEFAULT_HEIGHT="300px";
	public RichText() {
		this(true);
	}
	public RichText(boolean showToolBar) {
		
		stackPanel = new StackPanel();
		initWidget(stackPanel);
		VerticalPanel textPanel = new VerticalPanel();
		textArea = new RichTextArea();
		textArea.setSize("100%", "100%");
		
		
		if(showToolBar) {
			toolbar = new RichTextToolbar(textArea);
			toolbar.setWidth("100%");
			tpanel = new HorizontalPanel();
			tpanel.add(toolbar);
			textPanel.add(tpanel);
			textPanel.setCellHeight(tpanel, "50px");
		}
		textPanel.add(textArea);
	//	textPanel.setCellHeight(textArea,DEFAULT_HEIGHT);
		textPanel.setSize("100%", "100%");
		stackPanel.add(textPanel);
		
		html = new HTML();
		html.setHTML(textArea.getHTML());
		html.setSize("100%", "100%");
		html.setStyleName("richtext-html");
		stackPanel.add(html);
		
		stackPanel.setSize("100%", "100%");
		stackPanel.showStack(0);
		
		
	}

		
	public void setReadOnly(boolean flag) {
		if(flag) {
			html.setHTML(textArea.getHTML());
			stackPanel.showStack(1);
		}else {
			stackPanel.showStack(0);
		}
			
	}
	public void setHTML(String htmlTxt) {
		if(htmlTxt!=null) {
			textArea.setHTML(htmlTxt);
			html.setHTML(htmlTxt);
		}
		
	}
	public String getHTML() {
		// TODO Auto-generated method stub
		return textArea.getHTML();
	}
	public void setFocus(boolean flag) {
		textArea.setFocus(true);
		
	}
	
	public void disableImageButton() {
		if(toolbar!=null)
			toolbar.disableImageButton();
	  }
	  public void enableImageButton() {
		  if(toolbar!=null)
			  toolbar.enableImageButton();
	  }
	
	  public void addKeyboardListener(KeyboardListener listener) {
		  textArea.addKeyboardListener(listener);
	  }
	  
	  public void addFocusListener(FocusListener listener) {
		  textArea.addFocusListener(listener);
	  }
	  
	  public void addClickListener(ClickListener listener) {
		  textArea.addClickListener(listener);
	  }
	  
}
