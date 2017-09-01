package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAndCheckInAction;

public class CheckInDialog extends JDialog implements ActionListener,
		PropertyChangeListener {

	/** UID **/
	private static final long serialVersionUID = -7591128779785614597L;

	private JTextArea textArea;

	private JOptionPane optionPane;

	private String btnLabel1 = "OK";

	private String btnLabel2 = "Cancel";

	private JComboBox list;

	private String[] versionLabels = { "MAJOR", "MINOR" };

	private SaveAndCheckInAction action;

	/** Creates the reusable dialog. */
	public CheckInDialog(Frame aFrame, String title, SaveAndCheckInAction action) {
		super(aFrame, true);

		setTitle(title);

		this.action = action;

		list = new JComboBox(versionLabels);
		list.setSelectedIndex(0);

		JScrollPane areaScrollPane = configureAreaTextPane();

		
		Object[] array = { "Version", list, "Version note", areaScrollPane };

		// Create an array specifying the number of dialog buttons
		// and their text.
		Object[] options = { btnLabel1, btnLabel2 };

		// Create the JOptionPane.
		optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_OPTION, null, options, options[0]);

		// Make this dialog display it.
		setContentPane(optionPane);

		setLocation(400, 400);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textArea.requestFocusInWindow();
			}
		});

		// Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}

	private JScrollPane configureAreaTextPane() {
		textArea = new JTextArea(5, 20);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(150, 100));
		return areaScrollPane;
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnLabel1);
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY
						.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				// ignore reset
				return;
			}

			// Reset the JOptionPane's value.
			// If you don't do this, then if the user
			// presses the same button next time, no
			// property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

			if (btnLabel1.equals(value)) {
				String versionNote = textArea.getText();
				String versionType = (String) list.getSelectedItem();

				action.setVersionNote(versionNote);
				action.setVersionType(versionType);

				action.actionPerformed(null);
				clearAndHide();

			} else {
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		textArea.setText(null);
		setVisible(false);
	}

	@Override
	public void setVisible(boolean b) {
		DialogMacOsFix.installAppletFocusFix(this);
		super.setVisible(b);		
	}
}