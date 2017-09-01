package com.rsicms.rsuite.editors.oxygen.applet.domain.shortcut;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentDialogAction;

public class ShortCutManager {

	private Map<KeyStroke, Action> actionMap = new HashMap<KeyStroke, Action>();

	/** call this somewhere in your GUI construction */
	public void setup(OxygenMainComponent mainComponent) {
		KeyStroke key1 = KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_DOWN_MASK);
		actionMap.put(key1, new OpenNewDocumentDialogAction(mainComponent));

		KeyboardFocusManager kfm = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(createKeyEventDispatcher());
	}

	private KeyEventDispatcher createKeyEventDispatcher() {
		return new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(e);
				if (actionMap.containsKey(keyStroke)) {
					final Action action = actionMap.get(keyStroke);
					final ActionEvent ae = new ActionEvent(e.getSource(), e
							.getID(), null);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							action.actionPerformed(ae);
						}
					});
					return true;
				}
				return false;
			}
		};
	}

}
