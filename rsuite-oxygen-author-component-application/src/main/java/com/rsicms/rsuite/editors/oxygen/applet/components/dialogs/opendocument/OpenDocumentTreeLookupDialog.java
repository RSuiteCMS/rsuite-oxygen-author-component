package com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument;

import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.BOOKMAKRS_TAB_LABEL;
import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.BOOKMARKS_VIEW_TAB_DESC;
import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.BROWSE_DIALOG_TITLE;
import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.REPOSITORY_TAB_LABEL;
import static com.rsicms.rsuite.editors.oxygen.applet.components.tree.TreeDialogConstans.REPOSITORY_VIEW_TAB_DESC;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.bookmarks.IRepositoryBookmarks;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.components.custom.DescriptivePanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels.BookmarksTreeLookupPanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.opendocument.panels.RepositoryTreeLookupPanel;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.search.LookupDialog;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.opendocument.OpenNewDocumentAction;

public class OpenDocumentTreeLookupDialog extends LookupDialog {

	/** UID */
	private static final long serialVersionUID = 2897762892752763811L;

	private JTabbedPane tabbedPane;

	private ICmsURI cmsUri;

	private IRepositoryBookmarks repositoryBookmarks;

	private OpenNewDocumentAction openDocumentAction;

	public OpenDocumentTreeLookupDialog(JFrame mainFrame, ICmsURI cmsUri,
			ICmsCustomization cmsCustomization,
			final OpenNewDocumentAction openDocumentAction) {
		super(mainFrame);

		this.cmsUri = cmsUri;
		this.openDocumentAction = openDocumentAction;
		setName("dialog_open_new_document");

		setUpTabPanels(cmsCustomization);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(BROWSE_DIALOG_TITLE);

		setModal(true);
		setPreferredSize(new Dimension(600, 500));
		setLocation(100, 100);
	}

	private void setUpTabPanels(ICmsCustomization cmsCustomization) {
		repositoryBookmarks = cmsCustomization.getRepositoryBookmarks();

		List<DescriptivePanel> tabPanelsToAdd = new ArrayList<DescriptivePanel>();

		DescriptivePanel repositoryPanel = createRepositoryTabPanel(cmsCustomization
				.getRepositoryTreeLookup());
		tabPanelsToAdd.add(repositoryPanel);

		DescriptivePanel bookmarkPanel = createBookmarkTabPanel();
		tabPanelsToAdd.add(bookmarkPanel);
		setUpTabs(tabPanelsToAdd);
	}

	private void setUpTabs(List<DescriptivePanel> panelsToAdd) {

		tabbedPane = new JTabbedPane();
		tabbedPane.setName("tabbed_pane_open_document");

		for (DescriptivePanel panel : panelsToAdd) {
			tabbedPane.addTab(panel.getTitle(), null, panel.getPanel(),
					panel.getToolTiptext());
		}
		
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {	            
	            if (tabbedPane.getSelectedComponent() instanceof BookmarksTreeLookupPanel){
	            	((BookmarksTreeLookupPanel)tabbedPane.getSelectedComponent()).reloadBookmarks();
	            }
	        }
	    });

		add(tabbedPane);

	}

	private DescriptivePanel createRepositoryTabPanel(
			ITreeOxygenLookUp repositoryLookup) {

		JPanel repositoryPanel = new RepositoryTreeLookupPanel(this,
				repositoryLookup, repositoryBookmarks.getBookmarkManager(),
				openDocumentAction, cmsUri);

		return new DescriptivePanel(REPOSITORY_TAB_LABEL,
				REPOSITORY_VIEW_TAB_DESC, repositoryPanel);
	}

	private DescriptivePanel createBookmarkTabPanel() {

		ITreeOxygenLookUp bookmarksLookup = repositoryBookmarks
				.getBookmarkTreeLookup();

		JPanel bookmarksPanel = new BookmarksTreeLookupPanel(this,
				bookmarksLookup, repositoryBookmarks.getBookmarkManager(),
				openDocumentAction, cmsUri);
			

		return new DescriptivePanel(BOOKMAKRS_TAB_LABEL,
				BOOKMARKS_VIEW_TAB_DESC, bookmarksPanel);
	}

}
