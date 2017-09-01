package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import ro.sync.util.PlatformDetector;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.ApplicationTimer;

public class DialogMacOsFix {


	private static Logger logger = Logger.getLogger(DialogMacOsFix.class);
	
	/**
	 * Install a AWTListener on the dialog window that will trigger an
	 * WINDOW_ACTIVATED and WINDOW_FOCUS_GAINED events to reactivate the dialog
	 * when opened, in case it was deactivated by a bogus WINDOW_DEACTIVATED
	 * event.
	 * 
	 * @param toInstallOn
	 *            The dialog to install the fix on.
	 */
	public static void installAppletFocusFix(final JDialog toInstallOn) {
		
		String javaVersion = System.getProperty("java.version");
		
		if (PlatformDetector.isMacOS() &&				
				 javaVersion.startsWith("1.7")) {

			// This timer activation is more transparent to the user.
			toInstallOn.addWindowListener(new WindowAdapter() {
				@Override
				public void windowActivated(WindowEvent e) {
					toInstallOn.removeWindowListener(this);
					// A bogus Window deactivated will come a bit later so we
					// schedule a timer
					// to apply the fix.
					
					
					ApplicationTimer timer = new ApplicationTimer();
					Runnable task = new Runnable() {
						 
						@Override
						public void run() {
							try {
								SwingUtilities.invokeAndWait(new Runnable() {
									@Override
									public void run() {
										activateWindow(toInstallOn);
									}
								});
							} catch (InterruptedException e) {
								logger.error(e);
							} catch (InvocationTargetException e) {
								logger.error(e);
							}
						}
					};

					timer.schedule(task, 500);
				}
			});

			// When the mouse enters the window we check again if the previous
			// fix was successful or not.
			final AWTEventListener listener = new AWTEventListener() {
				@Override
				public void eventDispatched(AWTEvent event) {
					// Detect the owner window.
					Window window = null;
					Component comp = (Component) event.getSource();
					while (comp != null) {
						if (comp instanceof Window) {
							window = (Window) comp;
							break;
						}
						comp = comp.getParent();
					}

					if (window == toInstallOn) {
						activateWindow(toInstallOn);

						// Remove the listener.
						Toolkit.getDefaultToolkit()
								.removeAWTEventListener(this);
					}
				}
			};

			// Clean up listener.
			toInstallOn.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					// Just in case the wrapper will ever be reused.
					toInstallOn.removeWindowListener(this);
					// Make sure the listener on the toolkit is removed.
					Toolkit.getDefaultToolkit()
							.removeAWTEventListener(listener);
				}
			});

			Toolkit.getDefaultToolkit().addAWTEventListener(listener,
					AWTEvent.MOUSE_EVENT_MASK);
		}
	}

	/**
	 * Force window activation.
	 * 
	 * @param toInstallOn
	 *            The window to activate.
	 */
	private static void activateWindow(final JDialog toInstallOn) {
		final KeyboardFocusManager kfm = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		if (kfm.getFocusedWindow() == null || kfm.getActiveWindow() == null) {
			// The fact that we don't have an active or focused window indicates
			// the presence of the bug.
			try {
				kfm.dispatchEvent(new WindowEvent(toInstallOn,
						WindowEvent.WINDOW_ACTIVATED));
				kfm.dispatchEvent(new WindowEvent(toInstallOn,
						WindowEvent.WINDOW_GAINED_FOCUS));

				// Event though the KeyboardFocusManager knows who the focused
				// component is, we must request the focus again.
				Component permanentFocusOwner = kfm.getPermanentFocusOwner();
				if (permanentFocusOwner != null) {
					boolean descendingFrom = SwingUtilities.isDescendingFrom(
							permanentFocusOwner, toInstallOn);
					if (descendingFrom) {
						if (!permanentFocusOwner.requestFocusInWindow()) {
							permanentFocusOwner.requestFocus();
						}
					}
				}
			} catch (Throwable t) {
				logger.error(t, t);
			}
		}
	}

}
