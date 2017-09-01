package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ShowCheckInDialogAction;

public class OxygenMapManagerSystemToolbarButtons {

	public static final String BUTTON_SAVE = "save_button";

	public static final String BUTTON_CHECK_IN = "check_in_button";

	private Map<String, JComponent> systemButtonsMap = new LinkedHashMap<String, JComponent>();

	private OxygenMainComponent mainComponent;

	public OxygenMapManagerSystemToolbarButtons(OxygenMainComponent mainComponent) {
		this.mainComponent = mainComponent;
		addSystemCustomButtons();
	}

	public List<JComponent> getSystemButtons(){		
		return new ArrayList<JComponent>(systemButtonsMap.values());
	}
	
	public void changeButtonStatus(String buttonId, boolean enabled){
		JComponent button =  systemButtonsMap.get(buttonId);
		if (button != null){
			button.setEnabled(enabled);
		}
	}
	
	private void addSystemCustomButtons() {

		Icon icon = getIcon("save.gif");
		SaveAction saveAction = new SaveAction("Save document (Ctrl + s) ",
				icon, mainComponent, true);
		OxygenButton saveButton = new OxygenButton(saveAction);

		icon = getIcon("saved.gif");

		addButtonToMap(BUTTON_SAVE, saveButton);

		// actionsToolbar.add(saveButton);

		ShowCheckInDialogAction saveAndCheckInAction = new ShowCheckInDialogAction(
				"Save and check in document",
				OxygenUtils.getIcon("save-checkin-close.gif"), mainComponent);

		OxygenButton saveCheckInButton = new OxygenButton(saveAndCheckInAction);
		addButtonToMap(BUTTON_CHECK_IN, saveCheckInButton);

	}

	private Icon getIcon(String img) {
		return OxygenUtils.getIcon(img);
	}

	private void addButtonToMap(String key, JComponent button) {
		systemButtonsMap.put(key, button);
	}
}
