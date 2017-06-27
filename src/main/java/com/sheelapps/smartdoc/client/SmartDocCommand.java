package com.sheelapps.smartdoc.client;

import com.sheelapps.smartdoc.client.struct.Document;

public interface SmartDocCommand {

	public Document newDocument();

	public void save();

	public void toggleEditViewMode();

	public void editDocumentTitle();

	public void addNewTab();

	public void editTab();

	public void addNewChildNode();

	public void addNewSiblingNode();

	public void selectTab(String tabTitle, boolean fromHistory);
	
	public boolean isEditable();

	public void editNodeTitle();

	public void selectNode(String nodeTitle, boolean fromHistory);

	public void setNodeIcon(String icon);

	public void setTabIcon(String icon);

}
