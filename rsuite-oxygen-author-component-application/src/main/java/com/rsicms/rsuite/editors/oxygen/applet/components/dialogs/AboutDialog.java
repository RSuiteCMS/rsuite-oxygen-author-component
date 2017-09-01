package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.extension.helpers.OxygenInfo;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7298019113272489007L;
	private final JPanel contentPanel = new JPanel();



	/**
	 * Create the dialog.
	 */
	public AboutDialog(OxygenMainComponent mainComponent) {
		
		OxygenInfo info = mainComponent.getOxygenInfo();
		this.setTitle("About");
		setName("dialog_about");
		setModal(true);
		
		setPreferredSize(new Dimension(450, 200));
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JTextArea txtrCopyright = new JTextArea();
		txtrCopyright.setBackground(UIManager.getColor("ColorChooser.background"));
		txtrCopyright.setFont(new Font("Dialog", Font.BOLD, 12));
		txtrCopyright.setWrapStyleWord(true);
		txtrCopyright.setEditable(false);
		txtrCopyright.setLineWrap(true);
		txtrCopyright.setText("Copyright © 2015, RSI Content Solutions. All rights reserved.");
		
		String versionText = "Version: " + info.getVersion();
		JTextField lblVersion = createLabelText(versionText);
		
		JLabel lblNewLabel = new JLabel(OxygenUtils.getIcon("logo.png"));
		lblNewLabel.setDoubleBuffered(true);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(48)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtrCopyright, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
						.addComponent(lblVersion))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(33)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtrCopyright, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(18)
					.addComponent(lblVersion)
					.addContainerGap(119, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();

					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}



	private JTextField createLabelText(String text) {
		JTextField labelText = new JTextField(text);
		labelText.setEditable(false);
		labelText.setBorder(null);
		labelText.setForeground(UIManager.getColor("Label.foreground"));
		labelText.setFont(UIManager.getFont("Label.font"));
		return labelText;
	}
}
