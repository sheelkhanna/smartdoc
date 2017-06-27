package com.sheelapps.smartdoc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.sheelapps.gwt.gwtutils.client.HTMLHelper;
import com.sheelapps.gwt.gwtutils.client.MessageDialog;
import com.sheelapps.smartdoc.client.struct.Document;

public class SmartDocHeader extends Composite {

	private static final int ICON_COUNT = 10;
	private Label headerLabel;
	private Document doc;
	private HorizontalPanel panel;
	private SmartDocCommand cmd;
	private MenuItem saveItem;
	private MenuItem editTitleItem;
	private MenuItem addTabItem;
	private MenuItem editTabItem;
	private MenuItem addChildNodeItem;
	private MenuItem addSiblingNode;
	private String style;
	private MenuItem editNode;
	private MenuItem nodeIcon;
	private MenuItem tabIcon;
	private Command saveCommand;
	private Command editTitleCommand;
	private Command editTabCommand;
	private Command addTabCommand;
	private Command addChildNodeCommand;
	private Command addSiblingNodeCommand;
	private Command editNodeCommand;
	private MenuBar tabIconMenu;
	private MenuBar nodeIconMenu;
	private MenuItem toogleEditModeItem;
	private String iconPath = GWT.getModuleBaseURL()+"icons/docs/";
	
	public SmartDocHeader(SmartDocCommand editor, Document doc, String style) {
		this.doc=doc;
		this.cmd = editor;
		this.style=style;
		panel = new HorizontalPanel();
		panel.setSize("100%","100%");    
	    initWidget(panel);
	    addMenu();
		addTitle();
		showHideEditMenu();
	    
	}
	private void addTitle() {
		headerLabel = new Label();
	    headerLabel.setText(doc.getTitle()!=null ? doc.getTitle(): "");
		panel.add(headerLabel);
		panel.setCellWidth(headerLabel,"98%");
		panel.setCellHorizontalAlignment(headerLabel,HorizontalPanel.ALIGN_CENTER);
		
	}
	private void addMenu() {
		MenuBar bar = new MenuBar();
		bar.setAnimationEnabled(true);
		bar.setAutoOpen(true);
	    panel.add(bar);
	    panel.setCellWidth(bar,"2%");
	    panel.setCellHorizontalAlignment(bar,HorizontalPanel.ALIGN_LEFT);
	    MenuBar menu = new MenuBar(true);
	    menu.setStyleName(SmartDocConstants.STYLE_MENU+style);
	    menu.setAnimationEnabled(true);
	    menu.setAutoOpen(true);
	    addMenuItems(menu);
	    bar.addItem("[+]", menu);
		
	}
	
	
	private void addMenuItems(MenuBar menu) {
		
		menu.addItem("New SmartDoc Document", new Command()   {
			 
		    public void execute()
		    {
		    	doc = cmd.newDocument();
		    	
		    }
		});
		
		toogleEditModeItem = menu.addItem("Toggle Edit/View Mode", new Command()   {
			 
		    public void execute()
		    {
		    	
		    	cmd.toggleEditViewMode();
		    	showHideEditMenu();
		    }
		});
		
		menu.addItem("Show SmartDoc XML", new Command()   {
			 
		    public void execute()
		    {
		       MessageDialog.showInformation("SmartDoc XML",doc.toXML(),SmartDocConstants.STYLE_DIALOG+style);
		    	
		    }
		});
		
		menu.addSeparator();
		
		saveCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.save();
		    	
		    	
		    }
		};
		
		saveItem = menu.addItem("Save Changes", saveCommand);
	   
		
		editTitleCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.editDocumentTitle();	    	
		    }
		};
		editTitleItem = menu.addItem("Edit Document Title", editTitleCommand);
		
		
		menu.addSeparator();
		
		addTabCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.addNewTab();	    	
		    }
		};
		addTabItem = menu.addItem("Add Tab", addTabCommand) ;
		
		editTabCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.editTab();	    	
		    }
		};
		
		editTabItem = menu.addItem("Edit Tab Title", editTabCommand);
		
		tabIconMenu = getIcons(false);
		tabIcon = menu.addItem("Set Tab Icon",tabIconMenu);
				
		menu.addSeparator();
		
		
		addChildNodeCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.addNewChildNode();	    	
		    }
		};
		addChildNodeItem = menu.addItem("Add Child Node",addChildNodeCommand );
		
		
		addSiblingNodeCommand = new Command()   {
			 
		    public void execute()
		    {
		    	cmd.addNewSiblingNode();	    	
		    }
		};
		addSiblingNode  = menu.addItem("Add Sibling Node", addSiblingNodeCommand);
		
		
		editNodeCommand =  new Command()   {
			 
		    public void execute()
		    {
		    	cmd.editNodeTitle();	    	
		    }
		};
		editNode  = menu.addItem("Edit Node Title", editNodeCommand);
		
		nodeIconMenu = getIcons(true);
		nodeIcon = menu.addItem("Set Node Icon",nodeIconMenu);
	 
		
	}
	
	
	private MenuBar getIcons(boolean node) {
		MenuBar iconMenu = new MenuBar(true);
		iconMenu.setStyleName(SmartDocConstants.STYLE_MENU+style);
		
		MenuBar fileIconMenu = new MenuBar(true);
		fileIconMenu.setStyleName(SmartDocConstants.STYLE_MENU+style);
		addIcons(fileIconMenu,node, "file", 1 , 37);
		iconMenu.addItem("File",fileIconMenu);
		
		MenuBar peopleIconMenu = new MenuBar(true);
		peopleIconMenu.setStyleName(SmartDocConstants.STYLE_MENU+style);
		addIcons(peopleIconMenu,node, "people", 1 , 13);
		iconMenu.addItem("People", peopleIconMenu);
		
		MenuBar mmIconMenu = new MenuBar(true);
		mmIconMenu.setStyleName(SmartDocConstants.STYLE_MENU+style);
		addIcons(mmIconMenu, node, "multimedia", 1 , 12);
		iconMenu.addItem("Multimedia", mmIconMenu);
		
		return iconMenu;
	}
	private void addIcons(MenuBar iconMenu, boolean node, String path,  int startIndex, int totalSize) {
		int i=startIndex;
		MenuBar subIconMenu = new MenuBar(true);
		subIconMenu.setStyleName(SmartDocConstants.STYLE_MENU+style);
		if(startIndex+ ICON_COUNT <= totalSize) {
			iconMenu.addItem("More..",subIconMenu);
		}
		for(; i < startIndex + ICON_COUNT ; i++) {
		if(i > totalSize ) return;
		
		MenuItem item = null;
		Command setIconCmd = new setIconCommand(iconPath+path+"/"+i+".gif", cmd, node);		
		item = new MenuItem(HTMLHelper.image(iconPath+path+"/"+i+".gif") ,true, setIconCmd);
		item.setTitle(iconPath+path+"/"+i+".gif");
		iconMenu.addItem(item);
		}
		addIcons(subIconMenu, node, path, i, totalSize);
			
	}
	protected void setIcon(MenuItem item) {
		// TODO Auto-generated method stub
		
	}
	protected void showHideEditMenu() {
		if(cmd.isEditable()) {
			
			toogleEditModeItem.setText("View Mode");
			
			saveItem.setCommand(saveCommand);
			saveItem.setStyleName("gwt-MenuItem");
			
			editTitleItem.setCommand(editTitleCommand);
			editTitleItem.setStyleName("gwt-MenuItem");
			
			addTabItem.setCommand(addTabCommand);
			addTabItem.setStyleName("gwt-MenuItem");
			
			editTabItem.setCommand(editTabCommand);
			editTabItem.setStyleName("gwt-MenuItem");
			
			tabIcon.setStyleName("gwt-MenuItem");
			tabIconMenu.setVisible(true);
			
			addChildNodeItem.setCommand(addChildNodeCommand);
			addChildNodeItem.setStyleName("gwt-MenuItem");
			
			addSiblingNode.setCommand(addSiblingNodeCommand);
			addSiblingNode.setStyleName("gwt-MenuItem");
			
			editNode.setCommand(editNodeCommand);
			editNode.setStyleName("gwt-MenuItem");
			
			nodeIcon.setStyleName("gwt-MenuItem");
			nodeIconMenu.setVisible(true);
			
		}else {
			
			toogleEditModeItem.setText("Edit Mode");
			
			saveItem.setCommand(null);
			saveItem.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			editTitleItem.setCommand(null);
			editTitleItem.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			addTabItem.setCommand(null);
			addTabItem.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			editTabItem.setCommand(null);
			editTabItem.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			tabIcon.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			tabIconMenu.setVisible(false);
			
			addChildNodeItem.setCommand(null);
			addChildNodeItem.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			addSiblingNode.setCommand(null);
			addSiblingNode.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			editNode.setCommand(null);
			editNode.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			
			nodeIcon.setStyleName("gwt-MenuItem"+SmartDocConstants.STYLE_DISABLED);
			nodeIconMenu.setVisible(false);
			
		}
		
		/*
		editSeparator.setVisible(cmd.isEditable());
		saveItem.setVisible(cmd.isEditable());
		editTitleItem.setVisible(cmd.isEditable());
		tabIcon.setVisible(cmd.isEditable());
		addTabItem.setVisible(cmd.isEditable());
		editTabItem.setVisible(cmd.isEditable());
		addChildNodeItem.setVisible(cmd.isEditable());
		addSiblingNode.setVisible(cmd.isEditable());
		editNode.setVisible(cmd.isEditable());
		nodeIcon.setVisible(cmd.isEditable());
		*/		
	}
	public void setDocumentTitle(String title) {
		headerLabel.setText(title);
	}
	
    
	
}
