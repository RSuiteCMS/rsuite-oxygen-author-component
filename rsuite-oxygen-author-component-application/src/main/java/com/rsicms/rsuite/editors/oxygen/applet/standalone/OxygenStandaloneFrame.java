package com.rsicms.rsuite.editors.oxygen.applet.standalone;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;

public class OxygenStandaloneFrame extends JFrame {

	/** UID **/
	private static final long serialVersionUID = -990584327490560729L;

	private Logger logger = Logger.getLogger(getClass());
	
	public OxygenStandaloneFrame() {
		super("RSuite Oxygen Component");
	}

	private static final int JPANE_OPTION_CANCEL = 1;

	public void configureMainFrame(final OxygenMainComponent oxygenComponent) {

		addWindowListener(createWindowListener(oxygenComponent));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private WindowAdapter createWindowListener(
			final OxygenMainComponent oxygenComponent) {
		return new WindowAdapter() {
			@SuppressWarnings("static-access")
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();

				List<OxygenDocument> editedComponents = oxygenComponent
						.getEditedComponents();

				boolean isModified = false;

				for (OxygenDocument oxygenDocument : editedComponents) {
					isModified = isModified || oxygenDocument.isModified();
				}

				if (isModified) {

					Object[] options = { "Close Without Saving", "Cancel", };
					int option = JOptionPane.showOptionDialog(frame,
							"There is some unsaved content", "Warning",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);

					if (option == JOptionPane.YES_OPTION) {
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					} else if (option == JPANE_OPTION_CANCEL) {
						frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

					}
				}
			}
		};
	}

	public void bringFrameToFront() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					logger.error(e, e);
				}
				setVisible(false);
				toBack();
				repaint();
				setVisible(true);
				toFront();
				requestFocus();
				repaint();
			}
		});
		
	}
}
