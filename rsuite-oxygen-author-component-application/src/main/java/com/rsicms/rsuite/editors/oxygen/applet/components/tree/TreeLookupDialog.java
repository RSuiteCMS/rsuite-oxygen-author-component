package com.rsicms.rsuite.editors.oxygen.applet.components.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.DescriptivePanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.EditedRepositoryResource;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.RootRepositoryResource;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.BookmarkContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.ITreeContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.RepositoryContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.TreeContexMenuMouseAdapter;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeController;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.LazyLoadingTreeNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.lazy.TreeCellRenderer;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.ValidateAndAssignReferenceElementAction;

public class TreeLookupDialog extends LookupDialog implements ActionListener,
		ISelectableComponent, TreeDialogConstans {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	private static Logger logger = Logger.getLogger(TreeLookupDialog.class);

	private JTree activeTree;

	private JTree repostioryTree;

	private JTree bookmarkTree;

	// fileds used for the cross reference
	private JTabbedPane tabbedPane;

	private LinkTargetTabPanel linkTargetPanel;

	private String editedResourceId;

	private IDocumentURI documentUri;

	private OxygenDocument documentComponent;
	
	private IRepositoryBookmarks repositoryBookmarks;

	public TreeLookupDialog(JFrame mainFrame, AuthorAccess authorAccess,
			InsertReferenceElement element) {
		super(mainFrame);

		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);

		documentComponent = context.getDocumentComponent();
		documentUri = documentComponent.getDocumentUri();
		editedResourceId = documentUri.getEditedDocumentId();

		repositoryBookmarks = documentComponent.getCMSCustomization().getRepositoryBookmarks();
		
		List<DescriptivePanel> panelsToAdd = new ArrayList<DescriptivePanel>();

		DescriptivePanel repositoryPanel = createRepositoryPanel(authorAccess,
				element);
		panelsToAdd.add(repositoryPanel);

		if (element.isCrossReference()) {
			linkTargetPanel = new LinkTargetTabPanel(authorAccess, this,
					element);
			panelsToAdd.add(new DescriptivePanel(TARGET_ELEMENTS_TAB_LABEL,
					TARGET_ELEMENTS_VIEW_TAB_DESC, linkTargetPanel));
		}

		DescriptivePanel bookmarkPanel = createBookmarkPanel(authorAccess,
				element);
		panelsToAdd.add(bookmarkPanel);

		if (panelsToAdd.size() > 1) {
			setUpTabs(authorAccess, element, panelsToAdd);
		} else if (panelsToAdd.size() == 1) {
			add(panelsToAdd.get(0).getPanel());
		}

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(BROWSE_DIALOG_TITLE);

		setModal(true);
		setPreferredSize(new Dimension(600, 500));
		setLocation(100, 100);
	}

	private DescriptivePanel createRepositoryPanel(AuthorAccess authorAccess,
			InsertReferenceElement element) {

		ITreeOxygenLookUp repositoryLookup = documentComponent
				.getLookupRegister().getITreeOxygenLookUp(element);

		JTree tree = setUpTreeComponent(repositoryLookup);

		ITreeContextMenu contextMenu = new RepositoryContextMenu(repositoryBookmarks.getBookmarkManager());
		repostioryTree = tree;
		activeTree = tree;

		JPanel repositoryPanel = createTreePanel(authorAccess, element, tree,
				contextMenu, PANEL_NAME_REPOSITORY);
		return new DescriptivePanel(REPOSITORY_TAB_LABEL,
				REPOSITORY_VIEW_TAB_DESC, repositoryPanel);
	}

	private DescriptivePanel createBookmarkPanel(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		ITreeOxygenLookUp bookmarksLookup = repositoryBookmarks.getBookmarkTreeLookup();

		JTree tree = setUpTreeComponent(bookmarksLookup);
		tree.setName("bookmarksTree");

		ITreeContextMenu contextMenu = new BookmarkContextMenu(repositoryBookmarks.getBookmarkManager());
		bookmarkTree = tree;

		JPanel bookmakrsPanel = createTreePanel(authorAccess, element, tree,
				contextMenu, PANEL_NAME_BOOKMARKS);
		return new DescriptivePanel(BOOKMAKRS_TAB_LABEL,
				BOOKMARKS_VIEW_TAB_DESC, bookmakrsPanel);
	}

	private JPanel createTreePanel(AuthorAccess authorAccess,
			InsertReferenceElement element, JTree tree,
			ITreeContextMenu contextMenu, String name) {

		addContextMenu(tree, contextMenu);

		JPanel bookmarkPanel = setupTreePanel(authorAccess, element, tree, name);

		return bookmarkPanel;
	}

	private void addContextMenu(JTree tree,
			ITreeContextMenu repositoryContextMenu) {
		TreeContexMenuMouseAdapter treeContexMenuMouseAdapter = new TreeContexMenuMouseAdapter(
				repositoryContextMenu);
		tree.addMouseListener(treeContexMenuMouseAdapter);
	}

	public void setUpTabs(AuthorAccess authorAccess,
			InsertReferenceElement element, List<DescriptivePanel> panelsToAdd) {

		tabbedPane = new JTabbedPane();

		for (DescriptivePanel panel : panelsToAdd) {
			tabbedPane.addTab(panel.getTitle(), null, panel.getPanel(),
					panel.getToolTiptext());
		}

		ChangeListener changeListener = createChangeTabListener();
		tabbedPane.addChangeListener(changeListener);

		add(tabbedPane);
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
				Component activeComponent = sourceTabbedPane
						.getComponent(index);
				String activeComponentName = activeComponent.getName();

				if (PANEL_NAME_REPOSITORY.equals(activeComponentName)) {
					activeTree = repostioryTree;
				} else if (PANEL_NAME_BOOKMARKS.equals(activeComponentName)) {
					activeTree = bookmarkTree;
					loadBookmarksTree();
				}

				if (LinkTargetTabPanel.PANEL_NAME_LINK_TARGET
						.equals(activeComponentName)) {

					DefaultMutableTreeNode nodex = (DefaultMutableTreeNode) activeTree
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


	private void loadBookmarksTree() {
		try {

			Object rootObject = bookmarkTree.getModel().getRoot();
			
			if (rootObject instanceof DefaultMutableTreeNode){
				DefaultTreeModel model = (DefaultTreeModel) bookmarkTree.getModel();
				DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) rootObject;
				rootNode.removeAllChildren();
				model.reload();

				ITreeOxygenLookUp bookmarksLookup = repositoryBookmarks.getBookmarkTreeLookup();	
				List<IReposiotryResource> rootChildren = bookmarksLookup
						.getRootChildren();

				for (IReposiotryResource node : rootChildren) {

					LazyLoadingTreeNode lazyNode = new LazyLoadingTreeNode(
							node, model);
					rootNode.add(lazyNode);
				}
				
				bookmarkTree.expandRow(0);
			}

		} catch (Exception e) {
			OxygenUtils.handleException(logger, e);
		}
	}

	private JPanel setupTreePanel(AuthorAccess authorAccess,
			InsertReferenceElement element, JTree tree, String name) {

		JPanel panelLists = new JPanel();
		panelLists.setLayout(new BorderLayout());

		JPanel panelButtons = setupPanelButtons(authorAccess, element);
		panelButtons.setLayout(new GridLayout(1, 2));

		JScrollPane paneFiles = new JScrollPane(tree);
		panelLists.add(paneFiles, BorderLayout.CENTER);
		panelLists.add(panelButtons, BorderLayout.SOUTH);

		JLabel picLabel = null;

		if (element.isImage()) {
			picLabel = new JLabel("");
			picLabel.setPreferredSize(new Dimension(250, 250));
			picLabel.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
			JPanel p = new JPanel();
			p.setSize(200, 200);

			p.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			p.setAlignmentY(Component.CENTER_ALIGNMENT);
			picLabel.setSize(250, 250);
			p.add(picLabel);
			panelLists.add(p, BorderLayout.LINE_END);
		}

		JButton selectButton = getSelectButtonFromPanelButtons(panelButtons);
		TreeLookupSelectionListener treeLookupSelectionListener = new TreeLookupSelectionListener(
				documentUri.getCMSUri(), selectButton, picLabel);
		tree.addTreeSelectionListener(treeLookupSelectionListener);

		panelLists.setName(name);

		return panelLists;
	}

	private JButton getSelectButtonFromPanelButtons(JPanel panelButtons) {

		if (panelButtons.getComponentCount() > 0) {
			Component childComponent = panelButtons.getComponent(0);

			if (childComponent instanceof JPanel) {
				JPanel buttonsPanel = (JPanel) childComponent;
				for (Component child : buttonsPanel.getComponents()) {
					if (child instanceof JButton
							&& BUTTON_NAME_SELECT.equals(((JButton) child)
									.getName())) {
						return (JButton) child;
					}
				}
			}
		}

		return null;
	}

	private JTree setUpTreeComponent(ITreeOxygenLookUp treeLookup) {

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

	private JPanel setupPanelButtons(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		JPanel panelButtons = new JPanel();

		panelButtons.setLayout(new GridLayout(2, 1));
		JPanel panelButtonSelectTag = new JPanel();

		JButton buttonSelect = null;

		if (element.isCrossReference()) {
			buttonSelect = setUpButtonsForCrossReferences(panelButtonSelectTag);
		} else {
			buttonSelect = setUpDefaultButtons(authorAccess, element);
		}

		buttonSelect.setName(BUTTON_NAME_SELECT);

		JButton cancelButton = new JButton(CANCEL_BUTTON_LABEL);
		cancelButton.addActionListener(this);

		panelButtonSelectTag.add(buttonSelect);
		panelButtonSelectTag.add(cancelButton);

		panelButtons.add(panelButtonSelectTag);

		return panelButtons;
	}

	private JButton setUpDefaultButtons(AuthorAccess authorAccess,
			InsertReferenceElement element) {
		String buttonName = INSERT_ELEMEN_BUTTON_LABEL;

		if (element.isImage()) {
			buttonName = INSERT_IMAGE_BUTTON_LABEL;
		}

		JButton buttonSelectTargetElements = new JButton(buttonName);
		buttonSelectTargetElements.setEnabled(false);
		buttonSelectTargetElements
				.addActionListener(new ValidateAndAssignReferenceElementAction(
						authorAccess, this, element, this));

		return buttonSelectTargetElements;
	}

	private JButton setUpButtonsForCrossReferences(JPanel panelButtonSelectTag) {
		JButton buttonSelectTargetElements = new JButton(
				SHOW_TARGET_ELEMENTS_BUTTON_LABEL);
		buttonSelectTargetElements.setEnabled(false);
		buttonSelectTargetElements.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openLinkTargetTab();
			}
		});

		JButton buttonTargetTag = new JButton(SHOW_LOCAL_TARGETS_BUTTON_LABEL);
		buttonTargetTag.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
				linkTargetPanel.loadDataToPanel(new EditedRepositoryResource(
						editedResourceId));
			}
		});
		panelButtonSelectTag.add(buttonTargetTag);

		return buttonSelectTargetElements;
	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}

	public JTree getActiveTree() {
		return activeTree;
	}

	public void openLinkTargetTab() {
		if (tabbedPane != null) {
			tabbedPane.setSelectedIndex(1);
		}
	}

	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {
		DefaultMutableTreeNode nodex = (DefaultMutableTreeNode) activeTree
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
