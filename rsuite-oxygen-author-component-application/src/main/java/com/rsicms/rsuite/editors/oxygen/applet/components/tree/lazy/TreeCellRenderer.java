package com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy;

import java.awt.Component;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;

public class TreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Icon xmlIcon;

	private Icon imageIcon;
	
	private Icon folderIcon;
	
	private Map<String, Icon> iconMap = new HashMap<String, Icon>();

	public TreeCellRenderer(Icon folderIcon, Icon xmlIcon, Icon imageIcon) {
		this.xmlIcon = xmlIcon;
		this.imageIcon = imageIcon;
		this.folderIcon = folderIcon;

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		InputStream is = TreeCellRenderer.class.getResourceAsStream(path);

		
		try {
			if (is != null) {
				return new ImageIcon(IOUtils.toByteArray(is));
			}
		} catch (Exception e) {
			//print error in next line
		}
		
			System.err.println("Couldn't find file: " + path);
			return null;
		
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		setUpCustomIcons(value, leaf);

		return this;

	}

	public void setUpCustomIcons(Object value, boolean leaf) {
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if (node.getUserObject() instanceof IReposiotryResource) {
			IReposiotryResource repositoryResource = (IReposiotryResource) (node.getUserObject());
			Icon icon = getNodeIcon(repositoryResource);

			setToolTipText("ID: " + repositoryResource.getCMSid());
			
			if (icon == null){
				setDefaultIcon(repositoryResource, leaf);	
			}else{
				setIcon(icon);
			}
		}
	}

	private void setDefaultIcon(IReposiotryResource repositoryResource, boolean leaf) {
		if (imageIcon != null && xmlIcon != null) {

			if (leaf && !repositoryResource.isXml()) {
				setIcon(imageIcon);
			} else if (leaf) {
				setIcon(xmlIcon);
			}else {
				setIcon(folderIcon);
			}

		}
	}

	private Icon getNodeIcon(IReposiotryResource resource) {

			Icon icon = null;
			String iconName =  resource.getIconName();
			
			if (!StringUtils.isBlank(iconName)){
				icon = iconMap.get(iconName);
				if (icon == null){
					 icon = createImageIcon(iconName);
					 iconMap.put(iconName, icon);
				}
			}

			return icon;
		}
}
