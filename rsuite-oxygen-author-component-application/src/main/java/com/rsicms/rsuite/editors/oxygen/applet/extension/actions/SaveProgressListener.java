package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import javax.swing.JProgressBar;

public class SaveProgressListener {

	private JProgressBar progressBar;
	
	private long size = -1;
	

	public SaveProgressListener(JProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}
	
	public void setSize(long size){
		this.size = size;
	}

	public void updateTransferStatus(long transferredBytes){
		if (transferredBytes >= size){
			progressBar.setValue(100);
		}else{
			int value = (int) (((double)transferredBytes/(double)size) * 100);
			progressBar.setValue(value);
		}		
	}
}
