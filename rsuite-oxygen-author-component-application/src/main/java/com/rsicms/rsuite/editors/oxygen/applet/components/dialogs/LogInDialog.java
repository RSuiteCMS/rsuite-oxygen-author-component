package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.CmsLogInAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveAndCheckInAction;

public class LogInDialog extends JDialog implements ActionListener,
		PropertyChangeListener {

	/** UID **/
	private static final long serialVersionUID = -7591128779785614597L;

	private JTextArea textArea;

	private String btnLabel1 = "OK";

	private String btnLabel2 = "Cancel";

	private final JLabel labelUsername = new JLabel("Username");
	private final JLabel labelPassword = new JLabel("Password");

	private final JTextField textFieldUsername = new JTextField(15);
	private final JPasswordField passwordField = new JPasswordField();

	private final JButton buttonLogin = new JButton("Login");
	private final JButton jbtCancel = new JButton("Cancel");

	private final JLabel labelStatus = new JLabel(" ");

	private SaveAndCheckInAction action;

	/** Creates the reusable dialog. */
	public LogInDialog(final Frame aFrame, final ICmsActions cmsActions) {
		super(aFrame, true);

		textFieldUsername.setName("field_username");
		passwordField.setName("field_password");
		labelStatus.setName("label_status");
		
		setTitle("Log in dialog");
		setName("dialog_login");

		
		
		//this.action = action;

		JPanel px = new JPanel(new GridLayout(2, 1, 7, 2));
		JLabel sessionExpiredLabel = new JLabel(
				"Session has expired. Please log in");
		sessionExpiredLabel.setName("label_session_expired");
		px.add(sessionExpiredLabel);

		JPanel p3 = new JPanel(new GridLayout(2, 1, 6, 6));
		p3.add(labelUsername);
		p3.add(labelPassword);

		JPanel p4 = new JPanel(new GridLayout(2, 1, 6, 6));
		p4.add(textFieldUsername);
		p4.add(passwordField);

		JPanel p1 = new JPanel();
		p1.add(p3);
		p1.add(p4);

		JPanel p2 = new JPanel();
		p2.add(buttonLogin);
		// p2.add(jbtCancel);

		JPanel p5 = new JPanel(new BorderLayout());
		p5.add(p2, BorderLayout.EAST);
		p5.add(labelStatus, BorderLayout.NORTH);
		labelStatus.setForeground(Color.RED);
		labelStatus.setHorizontalAlignment(SwingConstants.CENTER);

		setLayout(new BorderLayout());
		add(px, BorderLayout.NORTH);
		add(p1, BorderLayout.CENTER);
		add(p5, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		buttonLogin.addActionListener(action);
		
		buttonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				labelStatus.setText("");
				
				boolean result = false;
				try {
					result = cmsActions.logInToCms(textFieldUsername.getText(), new String(passwordField.getPassword()));
					
					if (result) {
						aFrame.setVisible(true);
						setVisible(false);
					} else {
						labelStatus.setText("Invalid username or password");
					}
					
				} catch (OxygenIntegrationException e1) {
					//TODO
				}
				
				
			}
		});

		// and their text.
		Object[] options = { btnLabel1, btnLabel2 };

		// Create the JOptionPane.

		// Make this dialog display it.
		// setContentPane(optionPane);

		setLocation(400, 400);
		setSize(200, 200);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				// textArea.requestFocusInWindow();
			}
		});

		// Register an event handler that reacts to option pane state changes.
	}

	private JScrollPane configureAreaTextPane() {
		textArea = new JTextArea(5, 20);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setPreferredSize(new Dimension(150, 100));
		return areaScrollPane;
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		// if (isVisible()
		// && (e.getSource() == optionPane)
		// && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
		// JOptionPane.INPUT_VALUE_PROPERTY
		// .equals(prop))) {
		// Object value = optionPane.getValue();
		//
		// if (value == JOptionPane.UNINITIALIZED_VALUE) {
		// // ignore reset
		// return;
		// }
		// }
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