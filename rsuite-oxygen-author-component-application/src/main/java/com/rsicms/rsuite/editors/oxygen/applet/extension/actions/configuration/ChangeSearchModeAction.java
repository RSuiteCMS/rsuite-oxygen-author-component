package com.rsicms.rsuite.editors.oxygen.applet.extension.actions.configuration;

import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ATTRIBUTES;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ELEMENTS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponentDefaultBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenAdditionalViewContainer;
import com.rsicms.rsuite.editors.oxygen.applet.extension.configuration.OxygenConfiguration;

public class ChangeSearchModeAction implements ActionListener {

	private Logger logger = Logger.getLogger(this.getClass());

	private OxygenConfiguration oxygenConfiguration;

//	private Lau
	
	public ChangeSearchModeAction(OxygenConfiguration oxygenConfiguration) {
		this.oxygenConfiguration = oxygenConfiguration;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
//		JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
//		menuItem.get
//		try {
//			setVisibleSideView(view, menuItem.isSelected());
//		} catch (AuthorComponentException e1) {
//			OxygenUtils.handleException(logger, e1);
//		}
	}

	

	

}
