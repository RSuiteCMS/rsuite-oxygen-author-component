package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDitaMapsMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.helpers.SaveHelper;

public class SaveAction extends AbstractAction {


	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	private OxygenMainComponent component;
	
	private boolean ditaMapManager;
	
	public SaveAction(String toolTipText, Icon icon,
			OxygenMainComponent component, boolean ditaMapManager) {
		super(toolTipText, icon);
		this.component = component;
		this.ditaMapManager = ditaMapManager;
	}
	
	public SaveAction(String toolTipText, Icon icon,
			OxygenMainComponent component) {
		this(toolTipText, icon, component, false);
	}

	public SaveAction(OxygenMainComponent component) {
		this(component, false);
	}
	
	public SaveAction(OxygenMainComponent component, boolean ditaMapManager) {
		this.component = component;
		this.ditaMapManager = ditaMapManager;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		IOxygenDocument documentComponent = getActiveDocument(component, ditaMapManager);
		
		if (documentComponent.isModified()){	
		SaveHelper helper = new SaveHelper(component, documentComponent);
		helper.save();
	}	
	}

	protected static IOxygenDocument getActiveDocument(OxygenMainComponent component, boolean ditaMapManager) {
		IOxygenDocument documentComponent;
		
		if (ditaMapManager){
			OxygenDitaMapsMainComponent mapManager = component.getDitaMapManagerComponent();
			
			documentComponent =	 mapManager.getActiveDocumentComponent();
			
		}else{
		
			documentComponent = component.getActiveDocumentComponent();
		}
		return documentComponent;
	}


}
