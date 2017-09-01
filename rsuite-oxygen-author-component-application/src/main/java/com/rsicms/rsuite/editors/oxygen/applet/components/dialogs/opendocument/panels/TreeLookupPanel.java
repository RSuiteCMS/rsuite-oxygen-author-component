package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels;

import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.BUTTON_NAME_SELECT;
import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.CANCEL_BUTTON_LABEL;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ISelectedReferenceNode;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.components.ISelectableComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeLookupSelectionListener;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.SelectedNode;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.contextmenu.ITreeContextMenu;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentAction;

public abstract class TreeLookupPanel extends JPanel implements ISelectableComponent{
	
	/** Uid */
	private static final long serialVersionUID = -3287008842696069159L;

	private ActionListener cancelActionListener;
	
	private ActionListener submitActionListener;
	
	private TreeLookupComponent lookupTree;
	
	public TreeLookupPanel(JDialog parentDialog, ITreeOxygenLookUp treeLookup, ITreeContextMenu contextMenu, OpenNewDocumentAction openDocumentAction, ICmsURI cmsURI){
		cancelActionListener = new OpenDocumentTreeLookupDialogCancelListener(parentDialog);
		submitActionListener = new OpenDocumentTreeLookupDialogSubmitListener(parentDialog,  this, openDocumentAction);		
		lookupTree =  new TreeLookupComponent(getTreeComponentName(), treeLookup, contextMenu);
		
		initialize(lookupTree, getPanelName(), cmsURI);
	}

	protected abstract String getPanelName();

	protected abstract String getTreeComponentName();
	
	public void initialize(JTree tree, String name, ICmsURI cmsURI){
		setLayout(new BorderLayout());

		JPanel panelButtons = setupPanelButtons();
		panelButtons.setLayout(new GridLayout(1, 2));

		JScrollPane paneFiles = new JScrollPane(tree);
		add(paneFiles, BorderLayout.CENTER);
		add(panelButtons, BorderLayout.SOUTH);

		JLabel picLabel = null;

		JButton selectButton = getSelectButtonFromPanelButtons(panelButtons);
		TreeLookupSelectionListener treeLookupSelectionListener = new TreeLookupSelectionListener(
				cmsURI, selectButton, picLabel);
		tree.addTreeSelectionListener(treeLookupSelectionListener);

		setName(name);
		
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
	
	private JPanel setupPanelButtons() {
		JPanel panelButtons = new JPanel();

		panelButtons.setLayout(new GridLayout(2, 1));
		JPanel panelButtonSelectTag = new JPanel();

		JButton buttonSelect = new JButton("Open");
		buttonSelect.setName(BUTTON_NAME_SELECT);
		buttonSelect.setEnabled(false);
		buttonSelect.addActionListener(submitActionListener);

		JButton cancelButton = new JButton(CANCEL_BUTTON_LABEL);
		cancelButton.addActionListener(cancelActionListener);

		panelButtonSelectTag.add(cancelButton);
		panelButtonSelectTag.add(buttonSelect);

		panelButtons.add(panelButtonSelectTag);

		return panelButtons;
	}

	@Override
	public ISelectedReferenceNode getSelectedReferenceNode() {		
		DefaultMutableTreeNode nodex = (DefaultMutableTreeNode) lookupTree
				.getLastSelectedPathComponent();

		if (nodex == null)
			return null;

		IReposiotryResource userNode = null;
		Object nodeInfo = nodex.getUserObject();
		if (nodex.isLeaf() && nodeInfo instanceof IReposiotryResource) {			
			userNode = (IReposiotryResource) nodeInfo;
		}

		if (userNode != null && !userNode.isContainer()) {
			return new SelectedNode(userNode);
		}

		return null;
	}

	public TreeLookupComponent getLookupTree() {
		return lookupTree;
	}

}
