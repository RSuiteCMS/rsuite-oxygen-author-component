package com.rsicms.rsuite.editors.oxygen.applet.components.custom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import com.rsicms.rsuite.editors.oxygen.applet.common.references.LookupMethod;

public class LookupModeConfigurationMenuButton extends OptionMenuButtonCreator {

	public LookupModeConfigurationMenuButton() {
		super();
	};

	@Override
	protected JPopupMenu createMenu() {

		JPopupMenu submenu = new JPopupMenu();
		ButtonGroup group = new ButtonGroup();
		ActionListener action = createActionListener();

		for (LookupMethod lookupMethod : LookupMethod.values()) {

			JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(
					lookupMethod.getName());
			rbMenuItem.setActionCommand(lookupMethod.toString());

			if (lookupMethod == LookupMethod.getCurrentMethod()) {
				rbMenuItem.setSelected(true);
			}

			rbMenuItem.addActionListener(action);
			group.add(rbMenuItem);
			submenu.add(rbMenuItem);
		}

		return submenu;

	}

	private ActionListener createActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				LookupMethod.setCurrentMethod(LookupMethod.valueOf(command));
			}
		};
	}

}
