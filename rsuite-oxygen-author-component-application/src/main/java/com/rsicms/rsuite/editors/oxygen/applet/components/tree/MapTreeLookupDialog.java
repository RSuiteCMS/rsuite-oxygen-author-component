package com.rsicms.rsuite.editors.oxygen.applet.components.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.component.ditamap.DITAMapTreeComponentProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDitaMapDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.RootRepositoryResource;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeController;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.TreeCellRenderer;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertMapReferenceElementAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertMapReferenceElementAction.Position;

public class MapTreeLookupDialog extends JDialog implements ActionListener,
		TreeSelectionListener, ISelectableComponent, TreeDialogConstans {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	private static Logger logger = Logger.getLogger(MapTreeLookupDialog.class);

	private JTree tree;

	private JButton buttonSelectTag;

	// fileds used for the cross reference
	private JTabbedPane tabbedPane;

	private LinkTargetTabPanel linkTargetPanel;

	private String editedResourceId;

	private JLabel picLabel;

	private IDocumentURI documentUri;
	
	private OxygenDitaMapDocument ditaMapDocument;

	private DITAMapTreeComponentProvider ditaMapComponent;
	
	private Position position;
	
	public MapTreeLookupDialog(JFrame mainFrame, OxygenDitaMapDocument ditaMapDocument,
			Position position) {
		super(mainFrame);

		this.ditaMapDocument = ditaMapDocument;
		this.ditaMapComponent = ditaMapDocument.getDitaMapComponent();
		this.position = position;

		documentUri = ditaMapDocument.getDocumentUri();
		editedResourceId = documentUri.getEditedDocumentId();
		
		JPanel treePanel = setupTreePanel(new InsertReferenceElement("topicref", "href"));

		
			
		
			add(treePanel);

		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(BROWSE_DIALOG_TITLE);

		setModal(true);
		setPreferredSize(new Dimension(600, 500));
		setLocation(100, 100);
	}



	/**
	 * Creates a change listener for Tabs
	 * 
	 * @return
	 */
	public ChangeListener createChangeTabListener() {
		return new ChangeListener() {

			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent
						.getSource();
				int index = sourceTabbedPane.getSelectedIndex();

				if (index == 1) {

					DefaultMutableTreeNode nodex = (DefaultMutableTreeNode) tree
							.getLastSelectedPathComponent();

					if (nodex == null)
						return;

					IReposiotryResource userNode = null;
					Object nodeInfo = nodex.getUserObject();
					if (nodex.isLeaf()
							&& nodeInfo instanceof IReposiotryResource) {
						userNode = (IReposiotryResource) nodeInfo;
					}

					if (userNode != null && !userNode.isContainer()) {
						linkTargetPanel.loadDataToPanel(userNode);
					} else {
						linkTargetPanel.resetPanel();
					}

				}

			}

		};
	}

	private JPanel setupTreePanel(
			InsertReferenceElement element) {

		JPanel panelLists = new JPanel();
		panelLists.setLayout(new BorderLayout());

		tree = setUpTreeComponent(element);

		JPanel panelButtons = setupPanelButtons(element);
		panelButtons.setLayout(new GridLayout(1, 2));

		JScrollPane paneFiles = new JScrollPane(tree);
		panelLists.add(paneFiles, BorderLayout.CENTER);
		panelLists.add(panelButtons, BorderLayout.SOUTH);


		return panelLists;
	}

	private JTree setUpTreeComponent(InsertReferenceElement element) {
		
		// 
		ITreeOxygenLookUp treeLookup = ditaMapDocument.getLookupRegister()
				.getITreeOxygenLookUp(element);

		JTree tree = new JTree();

		setUpCustomTreeIcons(tree);

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
				treeLookup.getRootDisplayText(), true);

		rootNode.setUserObject(new RootRepositoryResource(treeLookup
				.getRootDisplayText(), treeLookup.getRootIcon()));

		DefaultTreeModel model = new DefaultTreeModel(rootNode);

		try {
			List<IReposiotryResource> rootChildren = treeLookup
					.getRootChildren();

			for (IReposiotryResource node : rootChildren) {

				LazyLoadingTreeNode lazyNode = new LazyLoadingTreeNode(node,
						model);
				rootNode.add(lazyNode);
			}
		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}

		final LazyLoadingTreeController controller = new LazyLoadingTreeController(
				model, treeLookup);

		tree.setModel(model);
		tree.addTreeWillExpandListener(controller);
		tree.addTreeSelectionListener(this);

		return tree;
	}

	private void setUpCustomTreeIcons(JTree tree) {
		ImageIcon imageIcon = TreeCellRenderer.createImageIcon("images/im.gif");
		ImageIcon xmlIcon = TreeCellRenderer
				.createImageIcon("images/xml-resource.gif");

		ImageIcon folderIcon = TreeCellRenderer
				.createImageIcon("images/folder.png");

		if (imageIcon != null && xmlIcon != null) {
			tree.setCellRenderer(new TreeCellRenderer(folderIcon, xmlIcon,
					imageIcon));
		}
	}

	private JPanel setupPanelButtons(
			InsertReferenceElement elemednt) {
		JPanel panelButtons = new JPanel();

		panelButtons.setLayout(new GridLayout(2, 1));
		JPanel panelButtonSelectTag = new JPanel();

		
		
		
			setUpDefaultButtons();
		

		JButton button = new JButton(CANCEL_BUTTON_LABEL);
		button.addActionListener(this);

		panelButtonSelectTag.add(buttonSelectTag);
		panelButtonSelectTag.add(button);

		panelButtons.add(panelButtonSelectTag);

		return panelButtons;
	}

	private void setUpDefaultButtons(
			) {
		String buttonName = INSERT_ELEMEN_BUTTON_LABEL;

	
		buttonSelectTag = new JButton(buttonName);
		buttonSelectTag.setEnabled(false);
		//TODO
		buttonSelectTag.addActionListener(new InsertMapReferenceElementAction(
				ditaMapDocument, this, new InsertReferenceElement("topicref", "href"), this, position));
	}


	public void actionPerformed(ActionEvent e) {
		dispose();
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		// this even can be used for image preview
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (node == null) {
			buttonSelectTag.setEnabled(false);
			return;
		}

		Object nodeInfo = node.getUserObject();

		if (node.isLeaf() && nodeInfo instanceof IReposiotryResource) {
			IReposiotryResource userNode = (IReposiotryResource) nodeInfo;
			if (!userNode.isContainer()) {
				buttonSelectTag.setEnabled(true);

				if (picLabel != null) {
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
						picLabel.setMaximumSize(new Dimension(300, 300));

					} catch (Exception e2) {
						OxygenUtils.handleException(logger , e2);
						picLabel.setText("Unable to preview");
					}
				}
			}

			return;
		}

		buttonSelectTag.setEnabled(false);
	}

	public JTree getTree() {
		return tree;
	}

	public void openLinkTargetTab() {
		if (tabbedPane != null) {
			tabbedPane.setSelectedIndex(1);
		}
	}

	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {
		DefaultMutableTreeNode nodex = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (nodex == null)
			return null;

		IReposiotryResource userNode = null;
		Object nodeInfo = nodex.getUserObject();
		if (nodex.isLeaf() && nodeInfo instanceof IReposiotryResource) {
			userNode = (IReposiotryResource) nodeInfo;
		}

		if (userNode != null && !userNode.isContainer()) {
			boolean contextNode = StringUtils.equalsIgnoreCase(
					editedResourceId, userNode.getCMSid());
			return new SelectedNode(userNode, null, contextNode);
		}

		return null;
	}
}
