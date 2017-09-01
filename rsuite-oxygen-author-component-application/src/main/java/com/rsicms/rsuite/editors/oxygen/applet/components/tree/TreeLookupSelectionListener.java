package com.rsicms.rsuite.editors.oxygen.applet.components.tree;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;

public class TreeLookupSelectionListener implements TreeSelectionListener {

	private Logger logger = Logger.getLogger(getClass());
	
	private JButton buttonSelectTargetElements;

	private JLabel picLabel;

	private ICmsURI cmsUri;
	
	public TreeLookupSelectionListener(ICmsURI cmsUri,
			JButton buttonSelectTargetElements, JLabel picLabel) {
		super();
		this.cmsUri = cmsUri;
		this.buttonSelectTargetElements = buttonSelectTargetElements;
		this.picLabel = picLabel;
	}



	@Override
	public void valueChanged(TreeSelectionEvent e) {
		JTree tree =  (JTree)e.getSource();
		
		// this even can be used for image preview
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		//TODO provide an interface 
		
		if (node == null) {
			buttonSelectTargetElements.setEnabled(false);
			return;
		}

		Object nodeInfo = node.getUserObject();

		if (node.isLeaf() && nodeInfo instanceof IReposiotryResource) {
			IReposiotryResource userNode = (IReposiotryResource) nodeInfo;

			if (!userNode.isContainer()) {
				buttonSelectTargetElements.setEnabled(true);

				if (picLabel != null) {
					try {

						ImageIcon icon = null;

						String id = userNode.getCustomMetadata("thumbnail");

						if (StringUtils.isBlank(id)) {
							id = userNode.getCMSid();
						}

						URL url = new URL(cmsUri.getImagePreviewUri(id));

						Image img = ImageIO.read(url);

						if (img != null) {

							int height = img.getHeight(null);
							int width = img.getWidth(null);

							double size = height > width ? height : width;
							double scale = 1;

							double maxSize = 250;

							if (size > 250) {
								scale = size / maxSize;

								int newHeigth = (int) ((double) height / scale);
								int newWidht = (int) ((double) width / scale);

								img = img.getScaledInstance(newWidht,
										newHeigth, Image.SCALE_FAST);
							}

							picLabel.setText("");
							icon = new ImageIcon(img);
						}

						picLabel.setIcon(icon);
						picLabel.setMaximumSize(new Dimension(300, 300));

					} catch (Exception e2) {
						OxygenUtils.handleException(logger, e2);
						picLabel.setText("Unable to preview");
					}
				}
			}

			return;
		}
	
		buttonSelectTargetElements.setEnabled(false);
		
	}

	

}
