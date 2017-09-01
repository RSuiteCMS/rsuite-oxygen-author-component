package com.rsicms.rsuite.editors.oxygen.applet.components.symbols;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class SymbolLookupDialog extends JDialog {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	private Logger logger = Logger.getLogger(this.getClass());

	public SymbolLookupDialog(JFrame mainFrame, AuthorAccess authorAccess) {
		super(mainFrame);

		try {
			SymbolPicker picker = new SymbolPicker(this, authorAccess);
			add(picker);

			setTitle("Symbols");
			setModal(true);
			setPreferredSize(new Dimension(600, 280));
			setLocation(400, 400);

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

	}

}
