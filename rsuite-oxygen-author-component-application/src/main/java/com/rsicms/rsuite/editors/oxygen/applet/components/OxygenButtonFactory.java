package com.rsicms.rsuite.editors.oxygen.applet.components;

import javax.swing.Action;

public class OxygenButtonFactory {

	public static OxygenButton createSystemDocumentAwareButton(Action action){
		return new OxygenButton(action, true);
	}
}
