package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.OptionMenuButtonCreator;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.LookupModeConfigurationMenuButton;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.ViewOptionMenuButton;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.AboutDialog;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ShowCheckInDialogAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ShowDialogAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentDialogAction;

public class OxygenSystemToolbarButtons implements RSuiteOxygenConstants{

	private Map<String, JComponent> systemButtonsMap = new LinkedHashMap<String, JComponent>();
	
	private Map<String, JComponent> endSystemButtonsMap = new LinkedHashMap<String, JComponent>();

	private OxygenMainComponent mainComponent;

	public OxygenSystemToolbarButtons(OxygenMainComponent mainComponent) {
		this.mainComponent = mainComponent;
		addSystemCustomButtons();
		addEndSystemCustomButtons();
	}

	public List<JComponent> getSystemButtons(){		
		return new ArrayList<JComponent>(systemButtonsMap.values());
	}
	
	
	public List<JComponent> getEndSystemButtonsMap() {
		return new ArrayList<JComponent>(endSystemButtonsMap.values());
	}

	public void changeButtonStatus(String buttonId, boolean enabled){
		JComponent button =  systemButtonsMap.get(buttonId);
		if (button != null){
			button.setEnabled(enabled);
		}
	}
	
	private void addEndSystemCustomButtons() {
		Icon icon = getIcon("info.gif");
		ShowDialogAction showDialogAction = new ShowDialogAction("Show about", icon, mainComponent, new AboutDialog(mainComponent)); 

		OxygenButton infoButton = new OxygenButton(showDialogAction);
		infoButton.setName(BUTTON_INFO);
		endSystemButtonsMap.put(BUTTON_INFO, infoButton);
	}
	
	private void addSystemCustomButtons() {

		OpenNewDocumentDialogAction openNewDocumentAction = new OpenNewDocumentDialogAction(
				"Open new document (Ctrl + o) ",
				OxygenUtils.getIcon("open.gif"), mainComponent);

		OxygenButton openNewDocumentButton = new OxygenButton("button_open_document", openNewDocumentAction);
		addButtonToMap(BUTTON_OPEN, openNewDocumentButton);
		
		
		Icon icon = getIcon("save.gif");
		SaveAction saveAction = new SaveAction("Save document (Ctrl + s) ",
				icon, mainComponent);
		OxygenButton saveButton = OxygenButtonFactory.createSystemDocumentAwareButton(saveAction);

		icon = getIcon("saved.gif");

		addButtonToMap(BUTTON_SAVE, saveButton);


		ShowCheckInDialogAction saveAndCheckInAction = new ShowCheckInDialogAction(
				"Save and check in document",
				OxygenUtils.getIcon("save-checkin-close.gif"), mainComponent);

		OxygenButton saveCheckInButton = OxygenButtonFactory.createSystemDocumentAwareButton(saveAndCheckInAction);
		addButtonToMap(BUTTON_CHECK_IN, saveCheckInButton);

		OptionMenuButtonCreator buttonCreator = new ViewOptionMenuButton(
				mainComponent);
		addButtonToMap(BUTTON_VIEWS,
				buttonCreator.createMenuButton("settings.gif", "View options"));

		
		OptionMenuButtonCreator lookupTypeButton = new LookupModeConfigurationMenuButton();
		addButtonToMap(LOOKUP_CONFIGURATION,
				lookupTypeButton.createMenuButton("lookup-configuration.png", "Insert link mode"));
		
	}

	private Icon getIcon(String img) {
		return OxygenUtils.getIcon(img);
	}

	private void addButtonToMap(String key, JComponent button) {
		systemButtonsMap.put(key, button);
	}
}
