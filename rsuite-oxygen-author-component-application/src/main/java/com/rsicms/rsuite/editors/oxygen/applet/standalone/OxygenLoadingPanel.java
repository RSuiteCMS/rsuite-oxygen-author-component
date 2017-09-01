package com.rsicms.rsuite.editors.oxygen.applet.standalone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class OxygenLoadingPanel extends JPanel {

	/** UID. */
	private static final long serialVersionUID = -5790903261450213510L;

	private BufferedImage rsuiteLogoImage;

	private BufferedImage oxygenLogoImage;

	public OxygenLoadingPanel() throws OxygenIntegrationException {

		try {
			setPreferredSize(new Dimension(350, 350));
			rsuiteLogoImage = ImageIO.read(OxygenUtils
					.getImageStream("logo_rsuite.gif"));
			oxygenLogoImage = ImageIO.read(OxygenUtils
					.getImageStream("logo_oxygen.png"));

			ImageIcon progressIcon = new ImageIcon("images/indicator.gif");

			JLabel progress = new JLabel(progressIcon);
			JLabel loadingLabel = new JLabel("loading...");

			setLayout(null);
			Insets insets = getInsets();
			Dimension size = progress.getPreferredSize();

			JProgressBar progressBar = new JProgressBar();
			MyProgressUI ui2 = new MyProgressUI();
			progressBar.setUI(ui2);
			progress.setPreferredSize(new Dimension(200, 20));
			progressBar.setForeground(Color.red);
			progressBar.setIndeterminate(true);
			progressBar.setBackground(Color.WHITE);

			progressBar.setBorder(BorderFactory.createEmptyBorder());

			size = progressBar.getPreferredSize();
			progressBar.setIndeterminate(true);

			progressBar.setBounds(240 + insets.left, 110 + insets.top, 220,
					size.height);
			add(progressBar);

			progress.setBounds(260 + insets.left, 110 + insets.top, size.width,
					size.height);

			size = loadingLabel.getPreferredSize();
			loadingLabel.setBounds(300 + insets.left, 165 + insets.top,
					size.width, size.height);

			add(progress);
			add(loadingLabel);

			setBackground(Color.white);

		} catch (IOException e) {
			throw new OxygenIntegrationException(e);
		}
	}

	private static class MyProgressUI extends BasicProgressBarUI {

		private Rectangle r = new Rectangle();

		@Override
		protected void paintIndeterminate(Graphics g, JComponent c) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			r = getBox(r);

			g.setColor(new Color(208, 33, 41));
			g.fillOval(r.x, r.y, r.width, r.height);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(rsuiteLogoImage, 10, 10, null);
		g.drawImage(oxygenLogoImage, rsuiteLogoImage.getWidth() + 40, 49, null);
	}
}
