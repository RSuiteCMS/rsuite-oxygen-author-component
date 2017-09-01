package com.rsicms.rsuite.editors.oxygen.launcher.session;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public class LogInDialog extends JDialog {

	/** UID **/
	private static final long serialVersionUID = -7591128779785614597L;

	private JTextArea textArea;

	private final JLabel labelUsername = new JLabel("Username");
	private final JLabel labelPassword = new JLabel("Password");

	private final JTextField textFieldUsername = new JTextField(15);
	private final JPasswordField passwordField = new JPasswordField();

	private final JButton buttonLogin = new JButton("Login");

	private final JLabel labelStatus = new JLabel(" ");

	/** Creates the reusable dialog. */
	public LogInDialog(final Frame aFrame,
			final CmsSessionManager sessionManager) {
		super(aFrame, true);

		textFieldUsername.setName("field_username");
		passwordField.setName("field_password");
		labelStatus.setName("label_status");

		setTitle("Log in dialog");
		setName("dialog_login");

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

		final LoginUIAction loginUIAction = new LoginUIAction(sessionManager,
				aFrame);
		passwordField.addKeyListener(new PasswordSubmitKeyListener(
				loginUIAction));

		buttonLogin.addActionListener(loginUIAction);

		setLocation(400, 400);
		setSize(200, 200);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		textArea.setText(null);
		setVisible(false);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	private final class LoginUIAction implements ActionListener {
		private final CmsSessionManager sessionManager;
		private final Frame aFrame;

		private LoginUIAction(CmsSessionManager sessionManager, Frame aFrame) {
			this.sessionManager = sessionManager;
			this.aFrame = aFrame;
		}

		@Override
		public void actionPerformed(ActionEvent event) {

			labelStatus.setText("");

			boolean result = false;
			try {
				result = sessionManager.createNewSession(null,
						textFieldUsername.getText(),
						new String(passwordField.getPassword()));

				if (result) {
					//aFrame.setVisible(true);
					setVisible(false);
				} else {
					labelStatus.setText("Invalid username or password");
				}

			} catch (OxygenApplicationException e1) {
				e1.printStackTrace();
			}

		}
	}
}