package com.sheelapps.smartdoc.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
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
import com.sheelapps.smartdoc.client.struct.Document;
import com.sheelapps.smartdoc.client.struct.DocumentTab;
import com.sheelapps.smartdoc.client.struct.DocumentTreeItem;

public class DocumentViewer extends Composite implements SmartDocCommand {

	private Document doc;
	private TabPanel tp;
	private FlowPanel topPanel;
	private HashMap<Integer,UITab> uiTabs = new HashMap<Integer, UITab>();
	private HashMap<String,UITab> histTab = new HashMap<String, UITab>();
	private UITab selectedUITab = null;
	
	
	public DocumentViewer(Document doc) {
		
		topPanel = new FlowPanel();
		this.doc=doc;
		initWidget(topPanel);
		topPanel.setSize("100%", "100%");
		//topPanel.setStyleName(SmartDocConstants.STYLE_SMARTDOC);
		initUI();
		
	}

	private void initUI() {
		buildTab();
	
	}

	private void buildTab() {
		tp = new TabPanel();
		tp.setStyleName(SmartDocConstants.STYLE_SMARTDOC_TAB);
		for (int i = 0; i < doc.getTabCount(); i++) {
			UITab t = addUITab(doc.getTab(i), i);
			uiTabs.put(i,t);
			histTab.put(doc.getTab(i).getTitle(),t);
		}
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
		dp.setStyleName(SmartDocConstants.STYLE_SMARTDOC);
		Widget h = getHeader();
		h.setStyleName(SmartDocConstants.STYLE_SMARTDOC_HEADER);
		dp.add(h, DockPanel.NORTH);
		dp.add(tp, DockPanel.CENTER);
		dp.setCellHeight(h, "1%");
		topPanel.add(dp);
		
		String tabTitle = BrowserUtil.parseHistoryToken(BrowserUtil.getHistoryString()).get(SmartDocConstants.TAB_TITLE_QS);
		if(tabTitle!=null) {
			selectTab(tabTitle);
		}else {
			tp.selectTab(0);
		}
		
	}
	private Widget getHeader() {
		Label lbl = new Label();
		lbl.setText(doc.getTitle()!=null ? doc.getTitle(): "");
		return lbl;
	}

	private void onSelectTab(int index) {
		selectedUITab = uiTabs.get(index);
		 SmartDocHistoryListener.newItem(this,doc.getTab(selectedUITab.index).getTitle(),null);
		// GWT bug due to deferred command, reset split position
		if(selectedUITab.hz!=null) {
			selectedUITab.hz.setSize("100%", "100%");
			selectedUITab.hz.setSplitPosition(SmartDocConstants.DEFAULT_SPLIT_POSITION);
		}
		updateText();
	}

	private void updateText() {
	
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
		tabPanel.setStyleName(SmartDocConstants.STYLE_SMARTDOC_TABPANEL);
		tabPanel.setSize("100%", "100%");
		Tree tree =null;
		HTML text = new HTML();
		text.setStyleName(SmartDocConstants.STYLE_DATA);
		text.setHTML(tab.getData());
		HorizontalSplitPanel hz=null;
		if(tab.getTreeItems().size() > 0) {
			hz = new HorizontalSplitPanel();
			hz.setStylePrimaryName(SmartDocConstants.STYLE_HZ_PANEL);
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
			   tp.add(tabPanel, HTMLHelper.imageWithText(tab.getTabIcon(), tab.getTitle()), true);
		else
			   tp.add(tabPanel, tab.getTitle());
		
		
		UITab uiTab= new UITab();
		uiTab.parent=tabPanel;
		uiTab.hz = hz;
		uiTab.text=text;
		uiTab.tree=tree;
		uiTab.index=index;
		return uiTab;
	}

	private Tree addTree(DocumentTab tab) {
		Tree tree = new Tree();
		tree.setStyleName(SmartDocConstants.STYLE_TREE);
		addTreeItems(tree,null, tab.getTreeItems());
		tree.addTreeListener(new TreeListener() {

			public void onTreeItemSelected(TreeItem item) {
				updateText();
				
			}

			public void onTreeItemStateChanged(TreeItem item) {
				// TODO Auto-generated method stub
				
			}

						
		});
		
		return tree;
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
		protected HTML text;
		protected Tree tree;
		protected Widget parent;
		protected int index=-1;
	}

		
  public void selectTab(String tabTitle) {
	  if(histTab.get(tabTitle)!=null) {
		  int index = histTab.get(tabTitle).index;
		  tp.selectTab(index);
	  }else {
		  tp.selectTab(0);
	  }
		
  }

public void addNewChildNode() {
	// TODO Auto-generated method stub
	
}

public void addNewSiblingNode() {
	// TODO Auto-generated method stub
	
}

public void addNewTab() {
	// TODO Auto-generated method stub
	
}

public void editDocumentTitle() {
	// TODO Auto-generated method stub
	
}

public void editTab() {
	// TODO Auto-generated method stub
	
}

public Document newDocument() {
	// TODO Auto-generated method stub
	return null;
}

public void toggleEditViewMode() {
	// TODO Auto-generated method stub
	
}

public void save() {
	// TODO Auto-generated method stub
	
}

public boolean isEditable() {
	// TODO Auto-generated method stub
	return false;
}

public void editNodeTitle() {
	// TODO Auto-generated method stub
	
}

public void selectNode(String nodeTitle) {
	// TODO Auto-generated method stub
	
}

public void selectNode(String nodeTitle, boolean fromHistory) {
	// TODO Auto-generated method stub
	
}

public void selectTab(String tabTitle, boolean fromHistory) {
	// TODO Auto-generated method stub
	
}

public void setNodeIcon(String icon) {
	// TODO Auto-generated method stub
	
}

public void setTabIcon(String icon) {
	// TODO Auto-generated method stub
	
}
}
