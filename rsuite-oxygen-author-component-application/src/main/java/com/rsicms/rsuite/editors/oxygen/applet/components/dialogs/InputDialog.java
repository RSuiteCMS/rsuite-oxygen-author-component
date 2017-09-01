package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InputTextAbstractAction;

public class InputDialog extends JDialog implements ActionListener,
		PropertyChangeListener {

	/** UID **/
	private static final long serialVersionUID = -7591128779785614597L;

	private JTextField textField;

	private JOptionPane optionPane;

	private String btnString1 = "OK";

	private String btnString2 = "Cancel";

	private String title;

	private InputTextAbstractAction action;

	/** Creates the reusable dialog. */
	public InputDialog(Frame aFrame, String title, String inputText,
			InputTextAbstractAction action, String defaultValue) {
		super(aFrame, true);

		setTitle(title);

		this.title = title;
		this.action = action;
		
		
		if (defaultValue != null){
			textField = new JTextField(defaultValue, 10);
		}else{
			textField = new JTextField(10);
		}
		

		
		// Create an array of the text and components to be displayed.
		String msgString = inputText;

		Object[] array = { msgString, "", textField };

		// Create an array specifying the number of dialog buttons
		// and their text.
		Object[] options = { btnString1, btnString2 };

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
				textField.requestFocusInWindow();
			}
		});

		// Register an event handler that puts the text into the option pane.
		textField.addActionListener(this);

		// Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		optionPane.setValue(btnString1);
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

			if (btnString1.equals(value)) {
				String ucText = textField.getText();

				if (ucText == null || "".endsWith(ucText)) {
					textField.selectAll();
					JOptionPane.showMessageDialog(InputDialog.this, "The "
							+ title + " cannot be empty", "Error",
							JOptionPane.ERROR_MESSAGE);
					
					textField.requestFocusInWindow();

				} else {					
					action.setInputText(ucText);
					action.actionPerformed(null);
					clearAndHide();

				}
			} else {				
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		textField.setText(null);
		setVisible(false);
	}
}