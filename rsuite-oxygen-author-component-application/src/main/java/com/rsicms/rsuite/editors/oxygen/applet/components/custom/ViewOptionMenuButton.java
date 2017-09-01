package com.rsicms.rsuite.editors.oxygen.applet.components.custom;

import java.util.Set;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponentDefaultBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.OxygenAdditionalViewContainer;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ChangeSideViewModeAction;

public class ViewOptionMenuButton extends OptionMenuButtonCreator {

	private final OxygenMainComponent mainComponent;

	public ViewOptionMenuButton(OxygenMainComponent mainComponent) {
		super();
		this.mainComponent = mainComponent;
	};

	@Override
	protected JPopupMenu createMenu() {
		JPopupMenu menu = new JPopupMenu();

		JCheckBoxMenuItem cbMenuItem = null;

		OxygenMainComponentDefaultBuilder componentBuilder = (OxygenMainComponentDefaultBuilder) mainComponent
				.getComponentBuilder();

		Set<AdditionalView> viewKeySet = componentBuilder
				.getViewContainerKeySet();

		for (AdditionalView view : viewKeySet) {
			OxygenAdditionalViewContainer container = componentBuilder
					.getAdditionalViewContainer(view);

			if (container != null) {
				cbMenuItem = new JCheckBoxMenuItem(view.getMenuDescription());
				cbMenuItem.setSelected(container.isVisible());
				cbMenuItem.addActionListener(new ChangeSideViewModeAction(
						mainComponent, view, componentBuilder));
				menu.add(cbMenuItem);
			}
		}

		return menu;
	}

}
