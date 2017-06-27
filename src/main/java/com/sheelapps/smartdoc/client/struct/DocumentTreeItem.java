package com.sheelapps.smartdoc.client.struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentTreeItem {

	private String itemText;
	private String itemIcon;
	private String data;
	private List<DocumentTreeItem> items;
	private DocumentTreeItem parent;
	
	public static final String XML_TAG_NODE="node";
	
	
	public DocumentTreeItem(String itemText) {
		this(null,itemText,null);
	}
	
	public DocumentTreeItem(DocumentTreeItem parent, String itemText) {
		this(parent,itemText,null);
	}
	public DocumentTreeItem(DocumentTreeItem parent, String itemText, String itemIcon) {
		setParent(parent);
		setItemText(itemText);
		setItemIcon(itemIcon);
		items = new ArrayList<DocumentTreeItem>();
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getItemText() {
		return itemText;
	}


	public void setItemText(String itemText) {
		this.itemText = itemText;
	}


	public String getItemIcon() {
		return itemIcon;
	}


	public void setItemIcon(String itemIcon) {
		this.itemIcon = itemIcon;
	}

	public DocumentTreeItem getParent() {
		return parent;
	}


	public void setParent(DocumentTreeItem parent) {
		this.parent = parent;
	}

	
	public int getItemCount() {
		return getItems().size();
	}
	public void addChildItem(DocumentTreeItem item) {
		item.setParent(this);
		getItems().add(item);
	}

	
	public boolean isRoot() {
		return getParent()==null;
	}
	
	
	public List<DocumentTreeItem> getItems() {
		return items;
	}

	public String toXML() {
		StringBuffer xml = new StringBuffer();
		xml.append("\r\n<"+XML_TAG_NODE+" "+Document.XML_TAG_TITLE+"=\""+getItemText()+"\" ");
		if(getItemIcon()!=null)
			xml.append(""+DocumentTab.XML_TAG_ICON+"=\""+getItemIcon()+"\" ");
		xml.append(">");
		xml.append("\r\n<"+Document.XML_TAG_DATA+"><![CDATA["+getData());
		xml.append("]]></"+Document.XML_TAG_DATA+">");
		Iterator<DocumentTreeItem> iter = getItems().iterator();
		while (iter.hasNext()) {
			DocumentTreeItem item = iter.next();
			xml.append(item.toXML());
		}
		xml.append("\r\n</"+XML_TAG_NODE+">\r\n");
		return xml.toString();
	}
}
