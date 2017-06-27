package com.sheelapps.smartdoc.client.struct;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sheel
 *
 */
public class Document {

	private String id;
	private String name;
	private String title;
	private List<DocumentTab> tabs;
	
	public static final String XML_TAG_ID="id";
	public static final String XML_TAG_NAME="name";
	public static final String XML_TAG_DATA="data";
	public static final String XML_TAG_TITLE="title";
	
	public Document(String name) {
		setName(name);
		setId(name+"_"+System.currentTimeMillis());
		tabs = new ArrayList<DocumentTab>();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public DocumentTab getTab(int index) {
		return (DocumentTab) getTabs().get(index);
	}
	
	public void addTab(DocumentTab tab) {
		tab.setIndex(getTabs().size());
		getTabs().add(tab);
	}
	
	public int getTabCount() {
		return getTabs().size();
	}
	
	public boolean removeTab(int index) {
		 return getTabs().remove(index)!=null;
	}
	
	public boolean removeTab(DocumentTab tab) {
		return getTabs().remove(tab);
	}
	
	private List<DocumentTab> getTabs() {
		return tabs;
	}

	public String toXML() {
		StringBuffer xml = new StringBuffer();
		xml.append("<smartdoc "+XML_TAG_NAME+"=\""+getName()+"\" "+XML_TAG_ID+"=\""+getId()+"\"  "+XML_TAG_TITLE+"=\""+(getTitle()!=null ? getTitle() : "")+"\" >");
		for (int i = 0; i < getTabCount(); i++) {
			xml.append(getTab(i).toXML());
		}
		xml.append("</smartdoc>\r\n");
		return xml.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
