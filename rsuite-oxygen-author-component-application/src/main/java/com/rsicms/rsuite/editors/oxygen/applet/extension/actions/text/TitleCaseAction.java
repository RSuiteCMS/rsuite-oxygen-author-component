package com.rsicms.rsuite.editors.oxygen.applet.extension.actions.text;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.access.AuthorEditorAccess;

public class TitleCaseAction extends AbstractAction {

	/** UID */
	private static final long serialVersionUID = 2095130326761878351L;

	public Logger logger = Logger.getLogger(this.getClass());

	private AuthorAccess authorAccess;

	public TitleCaseAction(String toolTipText, Icon icon,
			AuthorAccess authorAccess) {

		super(toolTipText, icon);
		initializeFields(authorAccess);

	}

	public TitleCaseAction(AuthorAccess authorAccess) {

		initializeFields(authorAccess);

	}

	public void initializeFields(AuthorAccess authorAccess) {
		this.authorAccess = authorAccess;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		AuthorDocumentController documentController = authorAccess
				.getDocumentController();

		AuthorEditorAccess editorAccess = authorAccess.getEditorAccess();

		int selectionStart = editorAccess.getSelectionStart();

		String seletectedText = editorAccess.getSelectedText();

		seletectedText = WordUtils.capitalizeFully(seletectedText);

		editorAccess.deleteSelection();
		documentController.insertText(selectionStart, seletectedText);
	}

}
