package com.sheelapps.smartdoc.client;

import com.google.gwt.user.client.Command;

public class setIconCommand implements Command {

	private String icon;
	private boolean node;
	private SmartDocCommand cmd;

	public setIconCommand(String icon, SmartDocCommand cmd, boolean node) {
		// TODO Auto-generated constructor stub
		this.icon = icon;
		this.cmd = cmd;
		this.node = node;
	}

	public void execute() {
		// TODO Auto-generated method stub
		if(node)
			cmd.setNodeIcon(icon);
		else
			cmd.setTabIcon(icon);
	}

}
