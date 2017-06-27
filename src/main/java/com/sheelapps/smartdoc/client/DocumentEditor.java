package com.sheelapps.smartdoc.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;
import com.sheelapps.gwt.gwtutils.client.BrowserUtil;
import com.sheelapps.gwt.gwtutils.client.HTMLHelper;
import com.sheelapps.gwt.gwtutils.client.InputDialog;
import com.sheelapps.gwt.gwtutils.client.richtext.RichText;
import com.sheelapps.smartdoc.client.struct.Document;
import com.sheelapps.smartdoc.client.struct.DocumentTab;
import com.sheelapps.smartdoc.client.struct.DocumentTreeItem;

public class DocumentEditor extends Composite implements SmartDocCommand {

	private Document doc;
	private TabPanel tp;
	private FlowPanel topPanel;
	private HashMap<Integer,UITab> uiTabs = new HashMap<Integer, UITab>();
	private HashMap<String,UITab> histTab = new HashMap<String, UITab>();
	private HashMap<String,TreeItem> histNode = new HashMap<String, TreeItem>();
	private UITab selectedUITab = null;
	private boolean isDirty;
	private SmartDocHeader header;
	public boolean editMode=true;
	private String style;
	private boolean fromTabHistory;
	private boolean fromNodeHistory;
		
	
	public DocumentEditor(Document doc, boolean editMode, String style) {
		this.doc=doc;
		this.editMode=editMode;
		this.style = style;
		//topPanel.setStyleName(SmartDocConstants.STYLE_SMARTDOC);
		initUI();
		initWidget(topPanel);
	}

	private void initUI() {
		topPanel = new FlowPanel();
		topPanel.setSize("100%", "100%");
		tp = new TabPanel();
		tp.setStyleName(SmartDocConstants.STYLE_SMARTDOC_TAB+style);
		tp.setSize("100%", "100%");
		tp.addTabListener(new TabListener() {

			public boolean onBeforeTabSelected(SourcesTabEvents sender,
					int tabIndex) {
				// TODO Auto-generated method stub
				return true;
			}

			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				// TODO Auto-generated method stub
				onSelectTab(tabIndex);
			}
			
		});
		
		
		DockPanel dp = new DockPanel();
		dp.setSize("100%", "100%");
		dp.setStyleName(SmartDocConstants.STYLE_SMARTDOC+style);
		header = new SmartDocHeader(this,doc,style);
		header.setStyleName(SmartDocConstants.STYLE_SMARTDOC_HEADER+style);
		dp.add(header, DockPanel.NORTH);
		dp.add(tp, DockPanel.CENTER);
		dp.setCellHeight(header, "1%");
		topPanel.add(dp);
	
		setDocument(doc);
		
	}

	public void setDocument(Document doc) {
		this.doc = doc;
		uiTabs = new HashMap<Integer, UITab>();
    	histTab = new HashMap<String, UITab>();
    	histNode = new HashMap<String, TreeItem>();
    	fromNodeHistory=false;
    	fromTabHistory=false;
    	selectedUITab = null;
    	isDirty=false;
    	header.setDocumentTitle(doc.getTitle()!=null ? doc.getTitle(): "");
		buildTab();
		String tabTitle = BrowserUtil.parseHistoryToken(BrowserUtil.getHistoryString()).get(SmartDocConstants.TAB_TITLE_QS);
		if(doc.getTabCount() > 0) {
			if(tabTitle!=null) {
				selectTab(tabTitle,false);
			}else {
				tp.selectTab(0);
			}
		}
		String nodeTitle = BrowserUtil.parseHistoryToken(BrowserUtil.getHistoryString()).get(SmartDocConstants.NODE_TITLE_QS);
		if(nodeTitle!=null)  {
		  selectNode(nodeTitle,false);
		}else {
			 if(selectedUITab!=null && selectedUITab.tree!=null && selectedUITab.tree.getItemCount() > 0) {
				 selectedUITab.tree.setSelectedItem(selectedUITab.tree.getItem(0), true);
				
			 }
		}
	}

	private void buildTab() {
		tp.clear();
		for (int i = 0; i < doc.getTabCount(); i++) {
			UITab t = addUITab(doc.getTab(i), i);
			uiTabs.put(i,t);
			histTab.put(doc.getTab(i).getTitle(),t);
		}
		
	}
	
	/**
	 * Method executes on Tab change
	 * @param index
	 */

	private void onSelectTab(int index) {
		selectedUITab = uiTabs.get(index);
		if(!fromTabHistory)
		  SmartDocHistoryListener.newItem(this,doc.getTab(selectedUITab.index).getTitle(),null);
		fromTabHistory=false;
		if(selectedUITab!=null ) {
			selectedUITab.text.setReadOnly(!editMode);
			// GWT bug due to deferred command, reset split position
			if(selectedUITab.hz!=null) {
				selectedUITab.hz.setSize("100%", "100%");
				selectedUITab.hz.setSplitPosition(SmartDocConstants.DEFAULT_SPLIT_POSITION);
			}
			updateText();
		}
	}

	private void updateText() {
		 updateLastNodeText();
		 if(selectedUITab.tree!=null) {
			TreeItem selectedTreeItem = selectedUITab.tree.getSelectedItem();
			if(selectedTreeItem!=null) {
					DocumentTreeItem docItem = (DocumentTreeItem) selectedTreeItem.getUserObject();
					selectedUITab.text.setHTML(docItem.getData());
					//link += "&"+SmartDocConstants.TREEITEM_TITLE_QS+"="+docItem.getItemText();
		    }
		 }
		  else {
					String docHtml = doc.getTab(selectedUITab.index).getData();
					if(docHtml!=null)
						selectedUITab.text.setHTML(docHtml);
					else
						selectedUITab.text.setHTML("");
				}
		
	}

	private UITab addUITab(DocumentTab tab, int index) {
		SimplePanel tabPanel = new SimplePanel();
		tabPanel.setStyleName(SmartDocConstants.STYLE_SMARTDOC_TABPANEL+style);
		tabPanel.setSize("100%", "100%");
		Tree tree =null;
		RichText text = new RichText();
		text.setStyleName(SmartDocConstants.STYLE_DATA+style);
		text.setHTML(tab.getData());
		
		text.addKeyboardListener(new KeyboardListener() {

			public void onKeyDown(Widget arg0, char arg1, int arg2) {
				// TODO Auto-generated method stub
				setDirty(true);
			}

			public void onKeyPress(Widget arg0, char arg1, int arg2) {
				// TODO Auto-generated method stub
				setDirty(true);
			}

			public void onKeyUp(Widget arg0, char arg1, int arg2) {
				// TODO Auto-generated method stub
				setDirty(true);
				
			}
			
		});
		
		text.addFocusListener(new FocusListener() {

			public void onFocus(Widget sender) {
				// TODO Auto-generated method stub
				
			}

			public void onLostFocus(Widget sender) {
				// TODO Auto-generated method stub
				updateLastNodeText();
			}

						
		});
		
		
		
		HorizontalSplitPanel hz=null;
		if(tab.getTreeItems().size() > 0) {
			hz = new HorizontalSplitPanel();
			hz.setStylePrimaryName(SmartDocConstants.STYLE_HZ_PANEL+style);
			tree = addTree(tab);
			hz.setLeftWidget(tree);
			hz.setRightWidget(text);
			hz.setSize("100%", "100%");
			hz.setSplitPosition(SmartDocConstants.DEFAULT_SPLIT_POSITION);
			
			tabPanel.add(hz);
		}
		else {
			tabPanel.add(text);
		}
		
		if(tab.getTabIcon()!=null)
			   tp.insert(tabPanel, HTMLHelper.imageWithText(tab.getTabIcon(), tab.getTitle()), true,index);
		else
			   tp.insert(tabPanel, tab.getTitle(),index);
		
		
		UITab uiTab= new UITab();
		uiTab.parent=tabPanel;
		uiTab.hz = hz;
		uiTab.text=text;
		uiTab.tree=tree;
		uiTab.index=index;
		return uiTab;
	}

	private Tree addTree(DocumentTab tab) {
		final Tree tree = new Tree() ; 
		 /*{
		      @Override
		      public void onBrowserEvent(Event event) {
		        if (getSelectedItem() != null) {
		          if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT) {
		            DOM.eventPreventDefault(event);
		            event.preventDefault();
		            showContextMenu(event);
		          }
		        }
		        super.onBrowserEvent(event);
		      }

		      @Override
		      protected void setElement(Element elem) {
		        super.setElement(elem);
		        sinkEvents(Event.BUTTON_RIGHT);
		      }
		    };
           */
	    
		tree.setStyleName(SmartDocConstants.STYLE_TREE+style);
		
		addTreeItems(tree,null, tab.getTreeItems());
		tree.addTreeListener(new TreeListener() {

			public void onTreeItemSelected(TreeItem item) {
				onSelectNode(item);
			}

			public void onTreeItemStateChanged(TreeItem item) {
				// TODO Auto-generated method stub
				
			}

						
		});
		
		return tree;
	}

	

	protected void onSelectNode(TreeItem item) {
		updateText();
		histNode.put(item.getText(), item);
		selectedUITab.lastSelectedItem = item;
		if(!fromNodeHistory)
			SmartDocHistoryListener.newItem(this,doc.getTab(selectedUITab.index).getTitle(),item.getText());
		fromNodeHistory=false;
			
		
	}

	private void addTreeItems(Tree tree , TreeItem parentItem, List<DocumentTreeItem> treeItems) {
		Iterator<DocumentTreeItem> iter = treeItems.iterator();
		while (iter.hasNext()) {
			DocumentTreeItem item =  iter.next();
			TreeItem uiItem = null;
			if (item.getItemIcon() != null)
				uiItem = new TreeItem(HTMLHelper.imageWithText(item.getItemIcon(), item.getItemText()));
			else 
				uiItem = new TreeItem(item.getItemText());
			
			uiItem.setUserObject(item);
			
			if(parentItem!=null)
				parentItem.addItem(uiItem);
			else
				tree.addItem(uiItem);
			addTreeItems(tree, uiItem , item.getItems());
		}
		
	}


	class UITab  {
		
		public HorizontalSplitPanel hz;
		protected RichText text;
		protected Tree tree;
		protected TreeItem lastSelectedItem;
		protected Widget parent;
		protected int index=-1;
	}

	
  
  
  protected void setDirty(boolean flag) {
		isDirty = flag;
	
	}
  
  
  private void updateLastNodeText() {
		//save text to current node
	  TreeItem lastSelectedItem = getLastSelectedNode();
		if(isDirty && lastSelectedItem!=null) {
			DocumentTreeItem docItem = (DocumentTreeItem) lastSelectedItem.getUserObject();
			docItem.setData(selectedUITab.text.getHTML());
			setDirty(false);
		}
	}
  
  private TreeItem getLastSelectedNode() {
	  TreeItem selectedTreeItem = null;
	  if(selectedUITab.tree!=null) {
		 selectedTreeItem = selectedUITab.lastSelectedItem;
	  }
	return selectedTreeItem;		
  }
  
  private TreeItem getSelectedNode() {
	  TreeItem selectedTreeItem = null;
	  if(selectedUITab.tree!=null) {
		 selectedTreeItem = selectedUITab.tree.getSelectedItem();
	  }
	return selectedTreeItem;		
  }

  private InputDialog getSmartDocInputDialog(String title, String value) {
		// TODO Auto-generated method stub
	   InputDialog dlg = new InputDialog(title,value);
	   dlg.setStyleName(SmartDocConstants.STYLE_INPUT_DIALOG+style);
	   return dlg;
	}
  
/// Command implementation  
  
public void save() {
	setDirty(true);
	updateLastNodeText();
}

public Document newDocument() {
	editMode=true;
	setDocument(ApplicationMain.blankDocument());
	header.showHideEditMenu();
	return doc;
}

public void editDocumentTitle() {
	final InputDialog dlg = getSmartDocInputDialog("Enter Document Title",doc.getTitle());
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				doc.setTitle(title);
				header.setDocumentTitle(title);
			}
	  }
	});
	dlg.center();
	dlg.show();
}



public void addNewTab() {
	final InputDialog dlg = getSmartDocInputDialog("Enter Tab Title","");
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				DocumentTab tab = new DocumentTab(title);
				DocumentTreeItem newDocNode = new DocumentTreeItem(SmartDocConstants.TITLE_DEFAULT_NODE);
				newDocNode.setData(SmartDocConstants.TEXT_DEFAULT);
		    	tab.addTreeItem(newDocNode);
		    	doc.addTab(tab);
				int index = doc.getTabCount();
				if(doc.getTabCount() > 0) index--;
				UITab t = addUITab(doc.getTab(index), index);
				uiTabs.put(index,t);
				histTab.put(doc.getTab(index).getTitle(),t);
				tp.selectTab(index);
				selectedUITab.text.setHTML("");
			}
		}
		
	});
	dlg.center();
	dlg.show();
	
}

public void editTab() {
	// TODO Auto-generated method stub
	final DocumentTab tab = doc.getTab(selectedUITab.index);
	final InputDialog dlg = getSmartDocInputDialog("Enter Tab Title",tab.getTitle());
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				tab.setTitle(title);
				tp.remove(selectedUITab.index);
				UITab uiTab = addUITab(tab, selectedUITab.index);
				uiTabs.put(selectedUITab.index,uiTab);
				histTab.put(title,uiTab);
				tp.selectTab(selectedUITab.index);
			}
	  }
	});
	dlg.center();
	dlg.show();
}

public void toggleEditViewMode() {
	editMode  = !editMode;
	selectedUITab.text.setReadOnly(!editMode);
}

public void addNewSiblingNode() {
	final InputDialog dlg = getSmartDocInputDialog("Enter Node Title","");
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				TreeItem selItem = getSelectedNode();
				if(selItem!=null) {
					TreeItem parentItem = selItem.getParentItem();
					TreeItem newItem = null;
					DocumentTreeItem newDocNode = new DocumentTreeItem(title);
					newDocNode.setData(SmartDocConstants.TEXT_DEFAULT);
					if(parentItem!=null) {
						newItem = parentItem.addItem(title);
						DocumentTreeItem docNode = (DocumentTreeItem) parentItem.getUserObject();
						docNode.addChildItem(newDocNode);
					}else {
						newItem = selectedUITab.tree.addItem(title);
						doc.getTab(selectedUITab.index).addTreeItem(newDocNode);
					}
					newItem.setUserObject(newDocNode);
					
					
				}
			}
		}
		
	});
	dlg.center();
	dlg.show();
	
}

public void addNewChildNode() {
	final InputDialog dlg = getSmartDocInputDialog("Enter Node Title","");
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				TreeItem selItem = getSelectedNode();
				if(selItem!=null) {
					TreeItem newItem = selItem.addItem(title);
					DocumentTreeItem newDocNode = new DocumentTreeItem(title);
					newDocNode.setData(SmartDocConstants.TEXT_DEFAULT);
					newItem.setUserObject(newDocNode);
					DocumentTreeItem docNode = (DocumentTreeItem) selItem.getUserObject();
					docNode.addChildItem(newDocNode);
					
				}
			}
		}
		
	});
	dlg.center();
	dlg.show();
	
}

public boolean isEditable() {
	// TODO Auto-generated method stub
	return editMode;
}

public void editNodeTitle() {
	TreeItem selItem = getSelectedNode();
	String title = null;
	if(selItem!=null) {
		title = selItem.getText();
	}
	final InputDialog dlg = getSmartDocInputDialog("Enter Node Title",title);
	dlg.setCallback(new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			String title = dlg.getValue();
			if(title!=null) {
				TreeItem selItem = getSelectedNode();
				if(selItem!=null) {
					selItem.setText(title);
					DocumentTreeItem docNode = (DocumentTreeItem) selItem.getUserObject();
					docNode.setItemText(title);
				
				}
			}
		}
		
	});
	dlg.center();
	dlg.show();
	
}


/**
 * History callback method	
 */
public void selectTab(String tabTitle , boolean fromHistory) {
  this.fromTabHistory = fromHistory;	
  // from history but tab didnt changed
  if(fromHistory && selectedUITab!=null) {
	  DocumentTab tab = doc.getTab(selectedUITab.index);
	  if(tabTitle.equals(tab.getTitle())) {
		  return;
	  }
	  
  }
  if(histTab.get(tabTitle)!=null) {
	  int index = histTab.get(tabTitle).index;
	  tp.selectTab(index);
  }else {
	  tp.selectTab(0);
  }
	
}

/**
 * History callback method
 */
public void selectNode(String nodeTitle, boolean fromHistory) {
	 this.fromNodeHistory = fromHistory;
	 if(selectedUITab!=null && selectedUITab.tree!=null) {
		 TreeItem item = histNode.get(nodeTitle);
		 if(item!=null) {
			 selectedUITab.tree.setSelectedItem(item, true);
		 }
	 }
	
}

public void setNodeIcon(String icon) {
	// TODO Auto-generated method stub
	TreeItem item = getSelectedNode();
	if(item!=null) {
		DocumentTreeItem docItem = (DocumentTreeItem) item.getUserObject();
		item.setHTML(HTMLHelper.imageWithText(icon, docItem.getItemText()));
		docItem.setItemIcon(icon);
	}
}

public void setTabIcon(String icon) {
	if(selectedUITab!=null) {
		final DocumentTab tab = doc.getTab(selectedUITab.index);
		tab.setTabIcon(icon);
		tp.remove(selectedUITab.index);
		UITab uiTab = addUITab(tab, selectedUITab.index);
		uiTabs.put(selectedUITab.index,uiTab);
		histTab.put(tab.getTitle(),uiTab);
		tp.selectTab(selectedUITab.index);
	}
	
}


}





