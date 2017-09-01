package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog{

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	private JProgressBar progressBar;
	
	public ProgressDialog(JFrame mainFrame, String title, int height, int width) {
		super(mainFrame);

		this.setTitle(title);

		 progressBar = new JProgressBar(0, 100);
	     progressBar.setValue(0);
	     progressBar.setStringPainted(true);
		
	     add(progressBar);
		setModal(true);
		setPreferredSize(new Dimension(width, height));
		setLocation(400, 400);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
}
