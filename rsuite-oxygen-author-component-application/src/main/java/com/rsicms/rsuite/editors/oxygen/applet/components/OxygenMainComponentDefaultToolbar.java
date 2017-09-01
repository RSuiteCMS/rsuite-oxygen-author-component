package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;
import ro.sync.exml.workspace.api.editor.page.author.actions.AuthorActionsProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.OptionMenuButtonCreator;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.TagModeOptionMenuButton;
import com.rsicms.rsuite.editors.oxygen.applet.layout.ModifiedFlowLayout;

public class OxygenMainComponentDefaultToolbar implements
		IOxygenComponentToolbar, RSuiteOxygenConstants {
	
	protected final EditorComponentProvider editorComponentProvider;

	protected final OxygenMainComponent mainComponent;

	/** The actions toolbar */
	private JToolBar actionsToolbar;

	private OxygenDocument documentComponent;

	/** Split pane for the outline and editing area. */
	// private OxygenMainComponentDefaultBuilder componentBuilder;

	private Map<String, JButton> toolbarButtonsMap = new HashMap<String, JButton>();

	public OxygenMainComponentDefaultToolbar(OxygenMainComponent mainComponent,
			OxygenDocument documentComponent) {
		this.editorComponentProvider = documentComponent
				.getEditorComponentProvider();
		this.mainComponent = mainComponent;
		this.documentComponent =documentComponent;
		actionsToolbar = new JToolBar();
		actionsToolbar.setFloatable(false);
		actionsToolbar.setLayout(new ModifiedFlowLayout(FlowLayout.LEFT));
		
		// this.componentBuilder = componentBuilder;
		configureToolbar();
	}

	public OxygenMainComponentDefaultToolbar(
			EditorComponentProvider editorComponentProvider,
			OxygenMainComponent mainComponent) {
		this.editorComponentProvider = editorComponentProvider;
		this.mainComponent = mainComponent;
		actionsToolbar = new JToolBar();
		actionsToolbar.setFloatable(false);
		// this.componentBuilder = componentBuilder;
		configureToolbar();
	}

	//

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentToolbar2
	 * #configureToolbar()
	 */
	@Override
	public void configureToolbar() {
		actionsToolbar.removeAll();

		addSystemCustomButtons();
		if (documentComponent.isEditable()){
			addStandardOxygenAction();
			addFrameworkActions();
		}
		addEndSystemCustomButtons();
	}

	protected void addFrameworkActions() {
		
		if (isAuthorPage()) {
		
			List<JToolBar> tbs = documentComponent.getEditorPage().createExtensionActionsToolbars();
			for (int i = 0; i < tbs.size(); i++) {
				for (Component component :  tbs.get(i).getComponents()){
					actionsToolbar.add(component);
				}
			}
		
		}
	}

	protected void addStandardOxygenAction() {
		AuthorActionsProvider actionsProvider = OxygenUtils
				.getAuthorComponentEditorPage(editorComponentProvider)
				.getActionsProvider();

		Map<String, Object> authorCommonActions = actionsProvider
				.getAuthorCommonActions();		
	
		actionsToolbar.addSeparator();

		addOxygenActionToToolbar(authorCommonActions, "Edit/Edit_Cut");
		addOxygenActionToToolbar(authorCommonActions, "Edit/Edit_Copy");
		addOxygenActionToToolbar(authorCommonActions, "Edit/Edit_Paste");
	
		actionsToolbar.addSeparator();

		
		addOxygenActionToToolbar(authorCommonActions, "Edit/Edit_Undo");
		addOxygenActionToToolbar(authorCommonActions, "Edit/Edit_Redo");
		
		actionsToolbar.addSeparator();

		
		addOxygenActionToToolbar(authorCommonActions, "Find/Find_replace");
		addOxygenActionToToolbar(authorCommonActions, "Spelling/Check_spelling");
		addOxygenActionToToolbar(authorCommonActions, "Edit/Insert_from_Character_Map");
		
		if (isAuthorPage()) {
			WSAuthorComponentEditorPage authorPage = (WSAuthorComponentEditorPage) editorComponentProvider
					.getWSEditorAccess().getCurrentPage();
		
		JToolBar reviewToolbar = documentComponent.getEditorPage().createReviewToolbar();
		for (Component component :  reviewToolbar.getComponents()){
			if (! (component instanceof JToolBar.Separator)){
				actionsToolbar.add(component);
			}			
		}	
		
		//CSS alternatives toolbar.
			JToolBar cssAlternatives = authorPage.createCSSAlternativesToolbar();
			actionsToolbar.add(cssAlternatives);
		}
		
		
		
	}

	private boolean isAuthorPage() {
		return EditorPageConstants.PAGE_AUTHOR.equals(editorComponentProvider
				.getWSEditorAccess().getCurrentPageID());
	}

	private void addOxygenActionToToolbar(Map<String, Object> authorCommonActions,
			String actionId) {
		actionsToolbar.add(new OxygenButton((Action) authorCommonActions
				.get(actionId)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentToolbar2
	 * #revalidate()
	 */
	@Override
	public void revalidate() {
		if (actionsToolbar.getParent() != null) {
			actionsToolbar.getParent().invalidate();
			actionsToolbar.revalidate();
		}
	}

	private void addSystemCustomButtons() {

		OxygenSystemToolbarButtons systemButtons = mainComponent.getSystemButtons();
		List<JComponent> buttonsList = systemButtons.getSystemButtons();
		
		for (JComponent button : buttonsList){			
			actionsToolbar.add(button);
		}
		systemButtons.changeButtonStatus(BUTTON_SAVE, documentComponent.isModified());
		systemButtons.changeButtonStatus(BUTTON_CHECK_IN, true);
		
		if (isAuthorPage()) {
			OptionMenuButtonCreator buttonCreator = new TagModeOptionMenuButton(documentComponent, mainComponent);
			actionsToolbar.add(buttonCreator.createMenuButton("FullTags16.gif", "Change tag mode"));
		}
	}
	
	private void addEndSystemCustomButtons() {
		OxygenSystemToolbarButtons systemButtons = mainComponent.getSystemButtons();
		List<JComponent> buttonsList = systemButtons.getEndSystemButtonsMap();
		for (JComponent button : buttonsList){			
			actionsToolbar.add(button);
		}
	}
	

	protected void addButtonToMap(String key, JButton button) {
		toolbarButtonsMap.put(key, button);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentToolbar2
	 * #getToolbarButton(java.lang.String)
	 */
	@Override
	public JButton getToolbarButton(String key) {
		return toolbarButtonsMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentToolbar2
	 * #getActionsToolbar()
	 */
	@Override
	public JToolBar getActionsToolbar() {
		return actionsToolbar;
	}



}
