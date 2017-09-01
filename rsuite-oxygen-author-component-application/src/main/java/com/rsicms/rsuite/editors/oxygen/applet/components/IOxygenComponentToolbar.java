package com.rsicms.rsuite.editors.oxygen.applet.components;

import javax.swing.JButton;
import javax.swing.JToolBar;

public interface IOxygenComponentToolbar {

	public abstract void configureToolbar();

	public abstract void revalidate();

	public abstract JButton getToolbarButton(String key);

	public abstract JToolBar getActionsToolbar();

}