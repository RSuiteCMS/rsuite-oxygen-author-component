package com.rsicms.rsuite.editors.oxygen.applet.components.custom;

import javax.swing.ButtonGroup;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.TagMode;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ChangeTagModeAction;

public class TagModeOptionMenuButton extends OptionMenuButtonCreator {

	private final OxygenDocument documentComponent;

	private final OxygenMainComponent mainComponent;

	public TagModeOptionMenuButton(OxygenDocument documentComponent,
			OxygenMainComponent mainComponent) {
		super();
		this.documentComponent = documentComponent;
		this.mainComponent = mainComponent;
	};

	@Override
	protected JPopupMenu createMenu() {
		JPopupMenu submenu = new JPopupMenu();

		ButtonGroup group = new ButtonGroup();

		TagMode currentTagMode = documentComponent.getTagMode();

		for (TagMode tagMode : TagMode.values()) {

			JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(
					tagMode.getMenuDescription());
			if (currentTagMode == tagMode) {
				rbMenuItem.setSelected(true);
			}

			rbMenuItem
					.addActionListener(new ChangeTagModeAction(mainComponent));
			group.add(rbMenuItem);
			submenu.add(rbMenuItem);
		}

		return submenu;
	}

}
