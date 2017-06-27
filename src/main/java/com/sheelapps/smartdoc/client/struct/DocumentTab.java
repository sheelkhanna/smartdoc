package com.sheelapps.smartdoc.client.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * A single tab in document. Contains list of treeItems.
 * @author Sheel
 *
 */
public class DocumentTab {

	private String title;
	private String tabIcon;
	private List<DocumentTreeItem> treeItems;
	private int index;
	private String data;
	
	
	public static final String XML_TAG_ICON="icon";
	
	
	public DocumentTab(String title) {
		setTitle(title);
		treeItems = new ArrayList<DocumentTreeItem>();
	}
	
	public int getIndex() {
		return index;
	}
	protected void setIndex(int index) {
		this.index = index;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	public void addTreeItem(DocumentTreeItem item) {
		if(item!=null) {
			item.setParent(null);
			getTreeItems().add(item);
		}
	}
	
	public void addTreeItem(DocumentTreeItem item, int index) {
		getTreeItems().add(index, item);
	}
	
	public boolean removeTreeItem(DocumentTreeItem item) {
		return getTreeItems().remove(item);
	}
	
	public boolean removeTreeItem(int index) {
		return getTreeItems().remove(index)!=null;
	}
	
	public int getItemCount() {
		return getTreeItems().size();
	}
	public List<DocumentTreeItem> getTreeItems() {
		return treeItems;
	}

	public String toXML() {
		StringBuffer xml = new StringBuffer();
		xml.append("\r\n<smarttab "+Document.XML_TAG_TITLE+"=\""+getTitle()+"\" ");
		if(getTabIcon()!=null)
			xml.append(XML_TAG_ICON+"=\""+getTabIcon()+"\" ");
		xml.append(">");
		if(getData()!=null) {
			xml.append("\r\n<"+Document.XML_TAG_DATA+"><![CDATA["+getData());
			xml.append("]]></"+Document.XML_TAG_DATA+">\r\n");
		}
		Iterator<DocumentTreeItem> iter = getTreeItems().iterator();
		while (iter.hasNext()) {
			DocumentTreeItem item =  iter.next();
			xml.append(item.toXML());
		}
		xml.append("</smarttab>\r\n");
		return xml.toString();
	}

	public String getTabIcon() {
		return tabIcon;
	}

	public void setTabIcon(String tabIcon) {
		this.tabIcon = tabIcon;
	}

	public void setData(String data) {
		this.data=data;
		
	}
	public String getData() {
		return data;
	}
	
}
