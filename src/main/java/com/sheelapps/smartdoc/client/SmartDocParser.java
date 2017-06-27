package com.sheelapps.smartdoc.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.sheelapps.smartdoc.client.struct.Document;
import com.sheelapps.smartdoc.client.struct.DocumentTab;
import com.sheelapps.smartdoc.client.struct.DocumentTreeItem;

public class SmartDocParser {

	public static Document parse(String smartDocText) {
		Document doc = null;
		 try {
				com.google.gwt.xml.client.Document dom = XMLParser.parse(smartDocText);
			    com.google.gwt.xml.client.Element docElement = dom.getDocumentElement();
			    // Must do this if you ever use a raw node list that you expect to be
			    // all elements.
			    XMLParser.removeWhitespace(docElement);
			    String name = docElement.getAttribute(Document.XML_TAG_NAME);
			    doc = new Document(name); 	
			    doc.setId(docElement.getAttribute(Document.XML_TAG_ID));
			    doc.setTitle(docElement.getAttribute(Document.XML_TAG_TITLE));
			    NodeList tabNodes = docElement.getChildNodes();
			    for(int i=0; i < tabNodes.getLength();i++) {
			    	Node tabNode = tabNodes.item(i);
			    	if(tabNode.getNodeType()==Node.ELEMENT_NODE) {
			    		doc.addTab(addTab(tabNode));
			    	}
			    }
			   // System.out.println(doc.toXML());
			 
			} catch (Throwable ex) {
				Window.alert("Failed to parser SmartDoc XML. \r\n "+ex.getMessage());
			}
		return doc;	
	}
	
	
	private static DocumentTab addTab(Node tabNode) {
		DocumentTab tab = new DocumentTab(tabNode.getAttributes().getNamedItem(Document.XML_TAG_TITLE).getNodeValue());
		if(tabNode.getAttributes().getNamedItem(DocumentTab.XML_TAG_ICON)!=null)
			tab.setTabIcon(tabNode.getAttributes().getNamedItem(DocumentTab.XML_TAG_ICON).getNodeValue());
		
		NodeList treeNodes = tabNode.getChildNodes();
		for(int j=0; j < treeNodes.getLength();j++) {
			 Node treeNode = treeNodes.item(j);
			 if(treeNode.getNodeType()==Node.ELEMENT_NODE || treeNode.getNodeType()==Node.CDATA_SECTION_NODE) {
				if(treeNode.getNodeName().equals(Document.XML_TAG_DATA)) {
					 tab.setData(treeNode.getFirstChild().getNodeValue());
				}
				else if(treeNode.getNodeName().equalsIgnoreCase(DocumentTreeItem.XML_TAG_NODE)) {
					tab.addTreeItem(addTreeItem(treeNode));
				}
			 }
		}
		
		return tab;
		
	}

	private static DocumentTreeItem addTreeItem(Node treeNode) {
		DocumentTreeItem item = new DocumentTreeItem(treeNode.getAttributes().getNamedItem(Document.XML_TAG_TITLE).getNodeValue());
		 if(treeNode.getAttributes().getNamedItem(DocumentTab.XML_TAG_ICON)!=null)
			 item.setItemIcon(treeNode.getAttributes().getNamedItem(DocumentTab.XML_TAG_ICON).getNodeValue());
		 
		 NodeList treeChilds = treeNode.getChildNodes();
		 for(int j=0; j < treeChilds.getLength();j++) {
			 Node treeChild = treeChilds.item(j);
			 if(treeChild.getNodeType()==Node.ELEMENT_NODE || treeChild.getNodeType()==Node.CDATA_SECTION_NODE) {
				 if(treeChild.getNodeName().equals(Document.XML_TAG_DATA)) {
					 item.setData(treeChild.getFirstChild().getNodeValue());
				 }else if(treeChild.getNodeName().equals(DocumentTreeItem.XML_TAG_NODE)) {
					 item.addChildItem(addTreeItem(treeChild));
				 }
			 }
				 
		 }
		 
		 return item;
		
	}
	
}
