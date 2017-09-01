package com.rsicms.rsuite.editors.oxygen.launcher.update;

import java.util.concurrent.CountDownLatch;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *  * Execute file download in a background thread and update the progress.  * @author
 * www.codejava.net  *  
 */
public class OxygenAplicationUpdateGuiTask extends SwingWorker<Void, Void>
		implements OxygenApplicationUpdaterProgressHandler {

	private CountDownLatch latch;

	private OxygenApplicationUpdater applicationUpdater;

	public OxygenAplicationUpdateGuiTask(OxygenApplicationUpdater applicationUpdater, CountDownLatch latch) {
		this.latch = latch;
		this.applicationUpdater = applicationUpdater;
	}

	@Override
	protected Void doInBackground() throws Exception {

		try {

			applicationUpdater.updateOxygenApplication(this);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Error downloading file: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			setProgress(0);
			cancel(true);
		} finally {
			latch.countDown();
		}

		return null;
	}

	@Override
	public void updateProgress(long downloadedBytes, long bytesToDownload) {

		if (bytesToDownload == 0) {
			setProgress(100);
			return;
		}

		int progress = (int) (downloadedBytes * 100 / bytesToDownload);
		setProgress(progress);
	}
}