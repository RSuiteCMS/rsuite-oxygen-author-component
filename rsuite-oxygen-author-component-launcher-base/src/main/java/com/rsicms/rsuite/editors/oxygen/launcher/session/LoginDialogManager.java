package com.rsicms.rsuite.editors.oxygen.launcher.session;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class LoginDialogManager {

	public static void showLoginDialog(final JFrame mainFrame,
			final CmsSessionManager sessionManager) {
		
		JDialog customDialog = new LogInDialog(mainFrame,
				sessionManager);
		customDialog.pack();
		customDialog.setVisible(true);
	}
}
