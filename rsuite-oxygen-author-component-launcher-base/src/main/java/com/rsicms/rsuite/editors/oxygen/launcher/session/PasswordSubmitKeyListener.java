package com.rsicms.rsuite.editors.oxygen.launcher.session;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PasswordSubmitKeyListener implements KeyListener {

	private ActionListener loginUIAction;
	
	public PasswordSubmitKeyListener(ActionListener loginUIAction) {
		this.loginUIAction = loginUIAction;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 10){
			loginUIAction.actionPerformed(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
