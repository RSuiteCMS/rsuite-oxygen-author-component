package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class InputTextAbstractAction extends AbstractAction {


	private String inputText;
	
	private static final long serialVersionUID = 1L;

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public InputTextAbstractAction(String toolTipText, Icon icon) {
		super(toolTipText, icon);
	}
	
	public InputTextAbstractAction() {
		super();
	}
}
