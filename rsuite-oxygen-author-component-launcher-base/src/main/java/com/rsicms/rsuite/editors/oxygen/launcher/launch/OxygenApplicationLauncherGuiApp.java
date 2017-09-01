package com.rsicms.rsuite.editors.oxygen.launcher.launch;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class OxygenApplicationLauncherGuiApp extends JFrame {

	private static OxygenApplicationLauncherGuiApp gui;

	/** Uid **/
	private static final long serialVersionUID = 6529147676471653756L;

	private OxygenApplicationLauncherPanel launcherPanel;

	public OxygenApplicationLauncherGuiApp() {
		super("Oxygen launcher");
		setUpView();
		setVisible(true);

	}

	private void setUpView() {

		launcherPanel = new OxygenApplicationLauncherPanel();
		add(launcherPanel);
		setSize(450, 250);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void runGuiLauncher(
			final OxygenApplicationLauncher applicationLauncher,
			OxygenApplicationStartupArguments startUpArguments
			) throws Exception {

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		final CountDownLatch latch = new CountDownLatch(1);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				latch.countDown();
			}
		});

		latch.await();
		
		gui.launchOxygenApplication(applicationLauncher, startUpArguments);
	}

	public void showWarningMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}
	
	public void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Error",
				JOptionPane.ERROR_MESSAGE);

	}

	public void launchOxygenApplication(OxygenApplicationLauncher launcher, 
			OxygenApplicationStartupArguments startUpArguments)
			throws OxygenApplicationException, IOException,
			InterruptedException {
		launcher.launchOxygenApplication(launcherPanel, startUpArguments);
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

}
