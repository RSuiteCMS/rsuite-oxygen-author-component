package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.image;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ResultItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.table.TableSelectionHandler;

public class ImageTableSelectionHandler extends TableSelectionHandler {

	private Logger logger = Logger.getLogger(getClass());
	
	private IDocumentURI documentUri;
	
	private JLabel picLabel;
	
	public ImageTableSelectionHandler(IDocumentURI documentUri, JLabel picLabel) {
		this.documentUri = documentUri;
		this.picLabel = picLabel;
	}

	@Override
	protected void customSelectAction(ResultItem selectedItem) {
		previewImage(selectedItem);
	}
	
	private void previewImage(IReposiotryResource userNode){
	
			try {

				ImageIcon icon = null;

				String id = userNode.getCustomMetadata("thumbnail");

				if (StringUtils.isBlank(id)) {
					id = userNode.getCMSid();
				}

				URL url = new URL(documentUri.getCMSUri().getImagePreviewUri(id));

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
				picLabel.setMaximumSize(new Dimension(250, 250));

			} catch (Exception e) {
				OxygenUtils.handleException(logger, e);
				picLabel.setText("Unable to preview");
			}
		
	}
}
