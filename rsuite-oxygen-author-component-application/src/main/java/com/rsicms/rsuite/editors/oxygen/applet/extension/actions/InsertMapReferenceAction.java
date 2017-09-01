package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenMapContext;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDitaMapDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.MapTreeLookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertMapReferenceElementAction.Position;

public class InsertMapReferenceAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	public Logger logger = Logger.getLogger(this.getClass());

	private OxygenDitaMapDocument mapDocument;
	
	private Position position;

	public InsertMapReferenceAction(String toolTipText, Icon icon,
			OxygenDitaMapDocument mapDocument) {
		super(toolTipText, icon);
		initializeFields(mapDocument); 

	}

	public InsertMapReferenceAction(OxygenDitaMapDocument mapDocument, Position position) {

		initializeFields(mapDocument);
		this.position = position;
		
	}

	public void initializeFields(OxygenDitaMapDocument mapDocument) {
		this.mapDocument = mapDocument;
		putValue(NAME, "Reference...");
		//putValue(SMALL_ICON);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

	
			OxygenMapContext context = OxygenComponentRegister
					.getRegisterOxygenEditorContext(mapDocument.getDitaMapComponent());
			JDialog customDialog = new MapTreeLookupDialog(context.getMainFrame(),
					mapDocument, position);
			customDialog.pack();
			customDialog.setVisible(true);
	
	}
	
	@Override
	public Object getValue(String key) {
		
		Object obj = super.getValue(key);
		return obj;
	}
	
}
