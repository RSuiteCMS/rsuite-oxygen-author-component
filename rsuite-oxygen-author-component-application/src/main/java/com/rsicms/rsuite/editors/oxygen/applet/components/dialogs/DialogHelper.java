package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DialogHelper {

	private JFrame parentFrame;

	public DialogHelper(JFrame parentFrame) {
		super();
		this.parentFrame = parentFrame;
	}
	
	public JDialog createInformationDialog(String title, String information,
			int height, int width) {
		final JDialog customDialog = new InformationDialog(parentFrame, title,
				information, height, width);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				customDialog.pack();
				customDialog.setVisible(true);
			}
		});

		return customDialog;
	}

	public ProgressDialog createProgressDialog(String title, int height,
			int width) {
		final ProgressDialog customDialog = new ProgressDialog(parentFrame,
				title, height, width);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				customDialog.pack();
				customDialog.setVisible(true);
			}
		});

		return customDialog;
	}

	public void showErrorMessage(String errorMessage) {
		showErrorMessage(parentFrame, errorMessage);
	}
	
	public static void showErrorMessage(JFrame parentFrame, String errorMessage) {
		JOptionPane.showMessageDialog(parentFrame, errorMessage, "ERROR",
				JOptionPane.ERROR_MESSAGE);
	}

	public void showInformationMessage(String information) {
		JOptionPane.showMessageDialog(parentFrame, information, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showWarningMessage(String information) {
		JOptionPane.showMessageDialog(parentFrame, information, "Warning",
				JOptionPane.WARNING_MESSAGE);
	}

	public void removeInformationDialog(final JDialog customDialog) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				customDialog.dispose();
			}
		});

	}
}
