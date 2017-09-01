package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

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

public class ChangeSideViewModeAction implements ActionListener {

	private Logger logger = Logger.getLogger(this.getClass());

	private AdditionalView view;

	private OxygenMainComponent component;

	private OxygenMainComponentDefaultBuilder componentBuilder;

	/** Split pane for the outline and editing area. */
	private JSplitPane editorAndOutlineSplit;

	/** Split pane for the attributes and elements lists. */
	private JSplitPane attributesAndElementsSplit;
	
	private JSplitPane outlineAndDitaMapSplit;

	private JSplitPane centerPanel;

	private JSplitPane editorAndReviewSplit;

	public ChangeSideViewModeAction(OxygenMainComponent component,
			AdditionalView viewName,
			OxygenMainComponentDefaultBuilder componentBuilder) {
		super();
		this.view = viewName;
		this.component = component;
		this.editorAndOutlineSplit = componentBuilder
				.getEditorAndOutlineSplit();
		this.attributesAndElementsSplit = componentBuilder
				.getAttributesAndElementsSplit();
		this.centerPanel = componentBuilder.getCenterPanel();
		this.componentBuilder = componentBuilder;

		editorAndReviewSplit = componentBuilder.getEditorAndReviewSplit();
		outlineAndDitaMapSplit = componentBuilder.getOutlineAndDitaMapSplit();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
		try {
			setVisibleSideView(view, menuItem.isSelected());
		} catch (AuthorComponentException e1) {
			OxygenUtils.handleException(logger, e1);
		}
	}

	public AdditionalView getView() {
		return view;
	}

	/**
	 * Controls the visibility state of the side views.
	 * 
	 * @param sideViewName
	 *            The name of the side view. Can be one of <code>outline</code>,
	 *            <code>attributes</code>, <code>elements</code>.
	 * @param visible
	 *            <code>true</code> makes the side view visible,
	 *            <code>false</code> otherwise.
	 * @throws AuthorComponentException
	 *             when the visibility change failed.
	 */
	public void setVisibleSideView(final AdditionalView sideViewName,
			final boolean visible) throws AuthorComponentException {
		final Exception[] ex = new Exception[1];
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {

					OxygenAdditionalViewContainer container = componentBuilder
							.getAdditionalViewContainer(sideViewName);

					switch (sideViewName) {
					case ELEMENTS:
					case ATTRIBUTES:
						if (visible) {
							attributesAndElementsSplit.setVisible(true);
						}
					case DITA_MAP_MANAGER:
					case OUTLINE:	
						if (visible){
							outlineAndDitaMapSplit.setVisible(true);
						}
					default:
						if (container != null) {
							container.setVisible(visible);
						}
						break;
					}

					OxygenAdditionalViewContainer elementsContainer = componentBuilder
							.getAdditionalViewContainer(ELEMENTS);
					OxygenAdditionalViewContainer attributesContainer = componentBuilder
							.getAdditionalViewContainer(ATTRIBUTES);

					if (!elementsContainer.isVisible()
							&& !attributesContainer.isVisible()) {
						attributesAndElementsSplit.setVisible(false);
					}
					
					OxygenAdditionalViewContainer ditaMapManager = componentBuilder
							.getAdditionalViewContainer(AdditionalView.DITA_MAP_MANAGER);
					OxygenAdditionalViewContainer outlineContainer = componentBuilder
							.getAdditionalViewContainer(AdditionalView.OUTLINE);
					
					//TODO !ditaMapManager.isVisible() && DITA Map Manager
					if ( !outlineContainer.isVisible()){
						outlineAndDitaMapSplit.setVisible(false);
					}
					
					//TODO ditaMapManager.isVisible() && DITA Map Manager
					if (outlineContainer.isVisible()){
						editorAndOutlineSplit.setDividerLocation(250);
					}else{
						editorAndOutlineSplit.setDividerLocation(250);
					}

					// Forces relayout.
					attributesAndElementsSplit.setDividerLocation(0.5);
					editorAndReviewSplit.setDividerLocation(0.8);
					centerPanel.setDividerLocation(0.8);
					component.invalidate();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							component.invalidate();
							component.revalidate();
							component.repaint();
						}
					});

				} catch (Exception e) {
					ex[0] = e;
				}
				return null;
			}
		});
		if (ex[0] != null) {
			throw new AuthorComponentException(ex[0]);
		}
	}

}
