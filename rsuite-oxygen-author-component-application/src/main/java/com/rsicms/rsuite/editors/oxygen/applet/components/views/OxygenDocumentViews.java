package com.rsicms.rsuite.editors.oxygen.applet.components.views;

import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ATTRIBUTES;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.ELEMENTS;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.OUTLINE;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.REVIEW;
import static com.rsicms.rsuite.editors.oxygen.applet.components.views.AdditionalView.VALIDATION_PROBLEMS;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.ecss.extensions.api.component.AuthorComponentProvider;
import ro.sync.ecss.extensions.api.component.EditorComponentProvider;
import ro.sync.ecss.extensions.api.component.PopupMenuCustomizer;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import ro.sync.exml.editor.EditorPageConstants;
import ro.sync.exml.workspace.api.editor.page.author.WSAuthorComponentEditorPage;
import ro.sync.exml.workspace.api.editor.validation.ValidationProblems;
import ro.sync.exml.workspace.api.editor.validation.ValidationProblemsFilter;

import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenComponentToolbar;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponentDefaultBuilder;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponentDefaultToolbar;

public class OxygenDocumentViews {

	private Logger logger = Logger.getLogger(this.getClass());

	private EditorComponentProvider editorComponentProvider;

	private OxygenMainComponent mainComponent;

	private Map<AdditionalView, OxygenAdditionalView> viewMap = new HashMap<AdditionalView, OxygenAdditionalView>();

	private IOxygenComponentToolbar componentToolbar;
	
	public OxygenDocumentViews(OxygenMainComponent mainComponent,
			OxygenDocument documentComponent) {
		this.editorComponentProvider = documentComponent.getEditorComponentProvider();

		viewMap.put(REVIEW, new OxygenAdditionalView(
				"Review",
				editorComponentProvider
						.getAdditionalEditHelper(AuthorComponentProvider.REVIEWS_PANEL_ID)));

		
		viewMap.put(ATTRIBUTES, new OxygenAdditionalView(
				"Attributes",
				editorComponentProvider
						.getAdditionalEditHelper(AuthorComponentProvider.ATTRIBUTES_PANEL_ID)));


		viewMap.put(ELEMENTS, new OxygenAdditionalView(
				"Elements",
				editorComponentProvider
						.getAdditionalEditHelper(AuthorComponentProvider.ELEMENTS_PANEL_ID)));
		
		viewMap.put(OUTLINE, new OxygenAdditionalView(
				"Outline",
				editorComponentProvider
						.getAdditionalEditHelper(AuthorComponentProvider.OUTLINER_PANEL_ID)));

		
		setUpValidationView();
		
		componentToolbar = new OxygenMainComponentDefaultToolbar(
				mainComponent, documentComponent);
	}

	protected void addPopupCustomizers() {

		if (EditorPageConstants.PAGE_AUTHOR.equals(editorComponentProvider
				.getWSEditorAccess().getCurrentPageID())) {
			((WSAuthorComponentEditorPage) editorComponentProvider
					.getWSEditorAccess().getCurrentPage())
					.setOutlinerPopUpCustomizer(new PopupMenuCustomizer() {
						public void customize(JPopupMenu menu) {
							menu.addSeparator();
							menu.add(new AbstractAction(
									"Selected Elements Info") {
								public void actionPerformed(ActionEvent e) {
									TreePath[] selectedPaths = OxygenMainComponentDefaultBuilder
											.getAuthorComponentEditorPage(
													editorComponentProvider)
											.getAuthorAccess()
											.getOutlineAccess()
											.getSelectedPaths(true);
									if (selectedPaths != null
											&& selectedPaths.length > 0) {
										StringBuffer info = new StringBuffer();
										for (int i = 0; i < selectedPaths.length; i++) {
											info.append("Node <"
													+ ((AuthorNode) selectedPaths[i]
															.getLastPathComponent())
															.getName() + ">\n");
										}
										JOptionPane.showMessageDialog(
												mainComponent, info.toString());
									}
								}
							});
						}
					});
		}
	}

	private void setUpValidationView() {
		// Validation problems model list
		final DefaultListModel dlm = new DefaultListModel();
		// Validation problems list
		final JList problemsList = new JList();
		JScrollPane validationProblemsScrollPanel = new JScrollPane(
				problemsList);
		problemsList.setModel(dlm);

		// Add validation problems filter to retain the validation problems list
		editorComponentProvider.getWSEditorAccess()
				.addValidationProblemsFilter(new ValidationProblemsFilter() {
					public void filterValidationProblems(
							ValidationProblems validationProblems) {
						List<DocumentPositionedInfo> l = validationProblems
								.getProblemsList();
						// Clear the old validation problems list
						dlm.clear();
						if (problemsList != null && l != null) {
							// Update the validation problems list
							for (int i = 0; i < l.size(); i++) {
								dlm.addElement(l.get(i));
							}
						}
					}
				});
		// Set list cell renderer
		problemsList.setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(
						list, value, index, isSelected, cellHasFocus);
				label.setText(((DocumentPositionedInfo) value)
						.getMessageWithSeverity());
				return label;
			}
		});
		// When double clicking on a validation problem, the corresponding
		// document
		// content will be selected
		problemsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DocumentPositionedInfo dpi = (DocumentPositionedInfo) dlm
							.get(problemsList.getSelectedIndex());
					if (dpi != null) {
						try {
							int[] startEndOffsets = ((WSAuthorComponentEditorPage) editorComponentProvider
									.getWSEditorAccess().getCurrentPage())
									.getStartEndOffsets(dpi);
							if (startEndOffsets != null) {
								// Select the content corresponding to the
								// validation problem
								((WSAuthorComponentEditorPage) editorComponentProvider
										.getWSEditorAccess().getCurrentPage())
										.select(startEndOffsets[0],
												startEndOffsets[1]);
							}
						} catch (BadLocationException e1) {
							OxygenUtils.handleException(logger, e1);
						}
					}
				}
			}
		});
		
		viewMap.put(VALIDATION_PROBLEMS, new OxygenAdditionalView(
				"Validation Problems", validationProblemsScrollPanel));
		
		
	}
	
	public IOxygenComponentToolbar getComponentToolbar() {
		return componentToolbar;
	}
	
	public OxygenAdditionalView getAdditionalView(AdditionalView view){
		return viewMap.get(view);
	}
	
	/**
	 * Reconfigure the actions toolbar
	 */
	public void reconfigureActionsToolbar() {
		 componentToolbar.configureToolbar();
		 componentToolbar.revalidate();
	}
	
}
