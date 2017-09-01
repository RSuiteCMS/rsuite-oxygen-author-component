package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.ASearchPanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.crossRefernce.SearchCroossRefernceLookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.image.SearchImageLookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.refernce.SearchLookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeLookupDialog;

public class LookupAction {

	public Logger logger = Logger.getLogger(this.getClass());

	private AuthorAccess authorAccess;

	private InsertReferenceElement element;
	
	private Window window;

	public LookupAction(Window window,
			AuthorAccess authorAccess, InsertReferenceElement element) {

		initializeFields(authorAccess, element);
		this.window = window;
	}
	
	
	public LookupAction(String toolTipText, Icon icon,
			AuthorAccess authorAccess, InsertReferenceElement element) {

		initializeFields(authorAccess, element);

	}

	public LookupAction(AuthorAccess authorAccess,
			InsertReferenceElement element) {

		initializeFields(authorAccess, element);

	}

	public void initializeFields(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		this.authorAccess = authorAccess;
		this.element = element;
	}

	public ISelectedReferenceNode selectReference() {

		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);

		LookupMethod lookup = LookupMethod.getCurrentMethod(element.getLookup());
		
		LookupDialog customDialog = null;
		if (lookup == LookupMethod.BROWSE) {
			customDialog = new TreeLookupDialog(context.getMainFrame(),
					authorAccess, element);
		} else {
			customDialog = getSearchDialog(context);
		}

		return customDialog.showDialog();

	}
	
	private LookupDialog getSearchDialog(OxygenEditorContext context) {
		LookupDialog customDialog = null;
		ASearchPanel.desing = false;

		JFrame mainFrame = context.getMainFrame();

		if (element.isImage()) {
			customDialog = new SearchImageLookupDialog(mainFrame, authorAccess,
					element);
		} else if (element.isConfRef() || element.isCrossReference()) {
			customDialog = new SearchCroossRefernceLookupDialog(
					context.getMainFrame(), authorAccess, element);
		} else {
			customDialog = new SearchLookupDialog(context.getMainFrame(),
					authorAccess, element);
		}
		return customDialog;
	}

}
