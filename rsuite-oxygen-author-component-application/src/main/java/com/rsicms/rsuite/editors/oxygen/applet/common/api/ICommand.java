package com.rsicms.rsuite.editors.oxygen.applet.common.api;

public interface ICommand {

	void execute() throws Exception;
	
	void executeAfter();
}
