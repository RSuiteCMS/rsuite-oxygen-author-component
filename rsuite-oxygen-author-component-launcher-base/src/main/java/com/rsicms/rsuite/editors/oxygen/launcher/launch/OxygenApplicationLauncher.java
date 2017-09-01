package com.rsicms.rsuite.editors.oxygen.launcher.launch;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplication;
import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;
import com.rsicms.rsuite.editors.oxygen.launcher.session.CmsSessionManager;
import com.rsicms.rsuite.editors.oxygen.launcher.session.LoginDialogManager;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenAplicationUpdateGuiTask;
import com.rsicms.rsuite.editors.oxygen.launcher.update.OxygenApplicationUpdater;

public class OxygenApplicationLauncher {

	private OxygenApplication oxygenApplication;
	
	private OxygenApplicationUpdater applicationUpdater;
	
	private CmsSessionManager sessionManager;
	
	public OxygenApplicationLauncher(OxygenApplication oxygenApplication, OxygenApplicationUpdater applicationUpdater, CmsSessionManager sessionManager) {
		this.applicationUpdater = applicationUpdater;
		this.oxygenApplication = oxygenApplication;
		this.sessionManager = sessionManager;	
	}

	

	public void launchOxygenApplication(OxygenApplicationLauncherPanel launcherPanel, OxygenApplicationStartupArguments startUpArguments)
			throws OxygenApplicationException {

		OxygenApplicationRunner launcher = new OxygenApplicationRunner(
				oxygenApplication);

		try{
		
		if (oxygenApplication.isOxygenApplicationLaunched()) {
			launcher.openDocumentInNewTab(startUpArguments.getDocumentIdToOpen());
		} else {
			
			if (!sessionManager.isValidSession()){
				//TODO
				LoginDialogManager.showLoginDialog(null, sessionManager);
			}
			
			if (sessionManager.isValidSession()){
				CountDownLatch latch = performApplicationUpdate(launcherPanel, oxygenApplication);
				latch.await();
				launcher.startNewOxygenApplication(startUpArguments.getStartupCommandLineArguments());	
			}else{
				JOptionPane.showMessageDialog(null, "You are not logged in to CMS. " +
						"\nUnable to run oXygen", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		}catch (InterruptedException e){
			throw new OxygenApplicationException(e);
		}
	}

	private CountDownLatch performApplicationUpdate(
			final OxygenApplicationLauncherPanel launcherPanel, final OxygenApplication oxygenApplication) {
		final CountDownLatch latch = new CountDownLatch(1);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateOxygenApplication(launcherPanel, latch, oxygenApplication);

			}
		});
		return latch;
	}

	private void updateOxygenApplication(OxygenApplicationLauncherPanel launcherPanel, CountDownLatch latch,
			OxygenApplication oxygenApplication) {

		try {
			
			
			OxygenAplicationUpdateGuiTask task = new OxygenAplicationUpdateGuiTask(
					 applicationUpdater, latch);
			task.addPropertyChangeListener(launcherPanel);
			task.execute();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error executing upload task: "
					+ ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
