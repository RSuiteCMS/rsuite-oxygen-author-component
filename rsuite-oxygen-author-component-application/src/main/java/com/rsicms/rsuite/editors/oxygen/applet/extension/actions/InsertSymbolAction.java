package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JDialog;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.symbols.SymbolLookupDialog;

public class InsertSymbolAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	public Logger logger = Logger.getLogger(this.getClass());

	private AuthorAccess authorAccess;

	public InsertSymbolAction(String toolTipText, Icon icon,
			AuthorAccess authorAccess) {

		super(toolTipText, icon);
		initializeFields(authorAccess);

	}

	public InsertSymbolAction(AuthorAccess authorAccess) {

		initializeFields(authorAccess);

	}

	public void initializeFields(AuthorAccess authorAccess) {
		this.authorAccess = authorAccess;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		boolean canInsert = OxygenUtils
				.checkIfCanInsertTextInCurrentContext(authorAccess);

		if (!canInsert) {
			authorAccess.getWorkspaceAccess().showInformationMessage(
					"Unable to insert symbol element in this context");
		} else {
			OxygenEditorContext context = OxygenComponentRegister
					.getRegisterOxygenEditorContext(authorAccess);

			JDialog customDialog = new SymbolLookupDialog(
					context.getMainFrame(), authorAccess);
			customDialog.pack();
			customDialog.setVisible(true);
		}
	}

}
