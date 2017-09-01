package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.helpers.SaveHelper;

public class SaveAndCheckInAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	public Logger logger = Logger.getLogger(this.getClass());

	private OxygenMainComponent component;
	
	private boolean ditaMapManager;

	private String versionNote;

	private String versionType;

	public SaveAndCheckInAction(String toolTipText, Icon icon,
			OxygenMainComponent component, boolean ditaMapManager) {
		super(toolTipText, icon);
		initializeFields(component);
		this.ditaMapManager = ditaMapManager; 
	}
	
	public SaveAndCheckInAction(String toolTipText, Icon icon,
			OxygenMainComponent component) {
		this(toolTipText, icon, component, false);
	}

	private void initializeFields(OxygenMainComponent component) {
		this.component = component;
	}

	public SaveAndCheckInAction(OxygenMainComponent component) {
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		IOxygenDocument documentComponent = SaveAction.getActiveDocument(component, ditaMapManager);
		SaveHelper helper = new SaveHelper(component, documentComponent);
		helper.saveCheckInClose(versionType, versionNote);

	}

	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

}
