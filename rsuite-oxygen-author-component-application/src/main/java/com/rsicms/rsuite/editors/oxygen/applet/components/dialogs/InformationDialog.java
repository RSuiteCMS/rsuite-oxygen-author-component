package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InformationDialog extends JDialog{

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	public InformationDialog(JFrame mainFrame, String title, String message,  int height, int width) {
		super(mainFrame);

		JLabel label = new JLabel(message);
		add(label);

		this.setTitle(title);

		setModal(true);
		setPreferredSize(new Dimension(width, height));
		setLocation(400, 400);
	}

}
