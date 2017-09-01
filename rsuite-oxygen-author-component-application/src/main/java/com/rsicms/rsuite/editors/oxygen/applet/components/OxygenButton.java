package com.rsicms.rsuite.editors.oxygen.applet.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class OxygenButton extends JButton {

	/** UID */
	private static final long serialVersionUID = -4645870653197534801L;

	private boolean documentButton;
	
	public OxygenButton(Icon icon) {

		super(icon);
		setFormatting();
	}
	
	public OxygenButton(String name, final Action action) {
		this(action);
		setName(name);
	}
	
	public OxygenButton(final Action action, boolean documentButton) {
		this(action);
		this.documentButton = documentButton;
	}

	public OxygenButton(final Action action) {
		super();

		if (action.getValue(Action.SMALL_ICON) != null) {
			putClientProperty("hideActionText", Boolean.TRUE);
		}

		setAction(action);

		if (getToolTipText() == null) {
			setToolTipText((String) action.getValue(Action.NAME));
		}

		setFormatting();
	}

	public boolean isFocusable() {
		return false;
	}

	public void setFormatting() {
		setFormatting(this);
	}

	public static void setFormatting(JButton button) {
		button.setHorizontalTextPosition(JButton.CENTER);
		button.setVerticalTextPosition(JButton.BOTTOM);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
	}

	public boolean isDocumentButton() {
		return documentButton;
	}
	
}
