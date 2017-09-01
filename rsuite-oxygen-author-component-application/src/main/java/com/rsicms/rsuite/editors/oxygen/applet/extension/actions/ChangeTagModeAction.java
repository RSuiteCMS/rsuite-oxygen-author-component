package com.rsicms.rsuite.editors.oxygen.applet.extension.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JRadioButtonMenuItem;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.views.TagMode;

public class ChangeTagModeAction implements ActionListener {

	private static Logger logger = Logger.getLogger(ChangeTagModeAction.class);

	private final OxygenDocument documentComponent;

	public ChangeTagModeAction(OxygenMainComponent component) {
		super();
		documentComponent = component.getActiveDocumentComponent();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JRadioButtonMenuItem menuItem = (JRadioButtonMenuItem) e.getSource();
		String mode = menuItem.getText();

		mode = mode.replace(' ', '_').toLowerCase();
		try {
			setTagsDisplayMode(TagMode.fromMenuName(mode));
		} catch (AuthorComponentException e1) {
			OxygenUtils.handleException(logger, e1);
		} catch (OxygenIntegrationException e2) {
			OxygenUtils.handleException(logger, e2);
		}
	}

	/**
	 * Changes the way the editor displays the XML tags.
	 * 
	 * @param mode
	 *            Can be one of <code>full_tags</code>,
	 *            <code>full_tags_with_attributes</code>, <code>no_tags</code>,
	 *            <code>partial_tags</code>.
	 * 
	 * @throws AuthorComponentException
	 *             when the change of the tags display fails.
	 */
	public void setTagsDisplayMode(final TagMode mode)
			throws AuthorComponentException {
		setDisplayTagMode(mode, documentComponent);
	}

	public static void setDisplayTagMode(final TagMode mode, final OxygenDocument documentComponent) throws AuthorComponentException{
		final Exception[] ex = new Exception[1];
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {

					getAction(documentComponent, mode.getOxygenActionName()).actionPerformed(null);
					documentComponent.setTagMode(mode);

				} catch (Exception e) {
					ex[0] = e;
					OxygenUtils.handleException(logger, e);
				}
				return null;
			}
		});
		if (ex[0] != null) {
			throw new AuthorComponentException(ex[0]);
		}
	}
	
	private static Action getAction(OxygenDocument documentComponent, String action) {
		return (Action) getCommonActions(documentComponent).get(action);
	}

	private static Map<String, Object> getCommonActions(OxygenDocument documentComponent) {
		EditorComponentProvider editorComponentProvider = documentComponent.getEditorComponentProvider();
		return OxygenUtils
				.getAuthorComponentEditorPage(editorComponentProvider)
				.getActionsProvider().getAuthorCommonActions();
	}
}
