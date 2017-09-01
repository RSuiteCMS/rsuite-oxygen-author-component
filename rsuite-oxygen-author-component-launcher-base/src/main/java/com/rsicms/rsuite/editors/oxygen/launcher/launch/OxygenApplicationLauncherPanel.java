package com.rsicms.rsuite.editors.oxygen.launcher.launch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class OxygenApplicationLauncherPanel extends JPanel implements
PropertyChangeListener {

	private JLabel status = new JLabel("Updating the oXygen application");

	private JProgressBar progressBar = new JProgressBar(0, 100);
	
	/** UID */
	private static final long serialVersionUID = -5219161320878473768L;

	public OxygenApplicationLauncherPanel() {
		setUpView();
	}
	
	
	private void setUpView() {
		GridBagLayout layout = new GridBagLayout();		
		setLayout(layout);
		
		setSize(new Dimension(400, 200));
		setBackground(Color.WHITE);
		 
		addStatusComponent(layout);
	    addProgressbarComponent(layout);   
	       
		setVisible(true);

	}

	private void addProgressbarComponent(GridBagLayout layout) {
		GridBagConstraints contaraint;
		progressBar.setPreferredSize(new Dimension(390, 40));
		progressBar.setStringPainted(true);
	    
	    contaraint = new GridBagConstraints();
		contaraint.weightx = 2;
		contaraint.weighty = 2;
		contaraint.gridx = 0;
		contaraint.gridy = 1;
		contaraint.insets = new Insets(10, 15, 0, 0);
		contaraint.anchor = GridBagConstraints.FIRST_LINE_START;
	    layout.setConstraints(progressBar,contaraint);
	    add(progressBar);
	}

	private void addStatusComponent(GridBagLayout layout) {
		GridBagConstraints contaraint = new GridBagConstraints();
		contaraint.weightx = 2;
		contaraint.weighty = 1;
		contaraint.gridx = 0;
		contaraint.gridy = 0;
		contaraint.insets = new Insets(10, 15, 0, 0);
		contaraint.anchor = GridBagConstraints.FIRST_LINE_START;
	    layout.setConstraints(status,contaraint);    
	    add(status);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("progress")) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
	
}
