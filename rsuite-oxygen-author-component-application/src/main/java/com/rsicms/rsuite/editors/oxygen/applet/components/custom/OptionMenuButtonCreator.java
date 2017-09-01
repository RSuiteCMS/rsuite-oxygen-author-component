package com.rsicms.rsuite.editors.oxygen.applet.components.custom;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public abstract class OptionMenuButtonCreator {

	public AbstractButton createMenuButton(String icon, String tooltipText) {

		final JToggleButton moreButton = new JToggleButton(getIcon(icon));

		moreButton.setHorizontalTextPosition(JToggleButton.CENTER);
		moreButton.setVerticalTextPosition(JToggleButton.BOTTOM);
		moreButton.setFocusPainted(false);
		moreButton.setBorderPainted(false);
		moreButton.setOpaque(false);
		moreButton.setToolTipText(tooltipText);

		moreButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					createAndShowMenu((JComponent) e.getSource(), moreButton);

				}
			}
		});
		moreButton.setFocusable(false);
		moreButton.setHorizontalTextPosition(SwingConstants.LEADING);

		return moreButton;

	}

	protected void createAndShowMenu(final JComponent component,
			final AbstractButton moreButton) {

		JPopupMenu menu = createAndConfigureOptionMenu(moreButton);
		menu.show(component, 0, component.getHeight());

	}

	protected JPopupMenu createAndConfigureOptionMenu(
			final AbstractButton moreButton) {

		JPopupMenu menu = createMenu();

		menu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				moreButton.setSelected(false);
			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				moreButton.setSelected(false);
			}
		});

		return menu;
	}

	protected abstract JPopupMenu createMenu();

	private Icon getIcon(String img) {
		return OxygenUtils.getIcon(img);
	}

}
