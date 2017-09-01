/*
 *  The Syncro Soft SRL License
 *
 *  Copyright (c) 1998-2007 Syncro Soft SRL, Romania.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistribution of source or in binary form is allowed only with
 *  the prior written permission of Syncro Soft SRL.
 *
 *  2. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  3. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  4. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Syncro Soft SRL (http://www.sync.ro/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  5. The names "Oxygen" and "Syncro Soft SRL" must
 *  not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact support@oxygenxml.com.
 *
 *  6. Products derived from this software may not be called "Oxygen",
 *  nor may "Oxygen" appear in their name, without prior written
 *  permission of the Syncro Soft SRL.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE SYNCRO SOFT SRL OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 */
package com.rsicms.rsuite.editors.oxygen.applet.components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.component.AuthorComponentException;
import ro.sync.ecss.extensions.api.component.AuthorComponentFactory;
import ro.sync.ecss.extensions.api.component.ditamap.DITAMapTreeComponentProvider;
import ro.sync.ecss.extensions.api.component.listeners.DITAMapTreeComponentListener;
import ro.sync.exml.workspace.api.editor.page.ditamap.DITAMapPopupMenuCustomizer;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefInfo;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefTargetInfo;
import ro.sync.exml.workspace.api.standalone.ditamap.TopicRefTargetInfoProvider;

import com.rsicms.rsuite.editors.oxygen.applet.common.DocumentCustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentCustomization;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.tree.connector.LookupFactoryRegister;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertMapReferenceAction;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.InsertMapReferenceElementAction.Position;
import com.rsicms.rsuite.editors.oxygen.applet.layout.ModifiedFlowLayout;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmatersNames;

/**
 * Sample making use of a DITA Map tree component.
 */
@SuppressWarnings("serial")
public class OxygenDitaMapDocument extends JPanel implements IOxygenDocument {

	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(OxygenDitaMapDocument.class
			.getName());

	/**
	 * The actions toolbar
	 */
	private JToolBar actionsToolbar;

	private String documentId;

	private IDocumentURI documentURI;

	private Set<String> notSupportedActions = new HashSet<String>();
	
	private boolean isModified;
	
	private OxygenDitaMapsMainComponent mapMainComponent;
	
	private LookupFactoryRegister lookupRegister;
	/**
	 * The author component.
	 */
	private DITAMapTreeComponentProvider ditaMapComponent;

	private IDocumentCustomization customization;
	
	private JTabbedPane tabbedPane;

	public OxygenDitaMapDocument(String documentId) {
		super();
		this.documentId = documentId;
	}

	/**
	 * Constructor. Builds the sample.
	 * 
	 * @throws AuthorComponentException
	 */
	public OxygenDitaMapDocument(
			OxygenDitaMapsMainComponent mapMainComponent,
			AuthorComponentFactory factory,
			ICustomizationFactory customizationFactory,
			OxygenOpenDocumentParmaters parameters,
			JTabbedPane tabbedPane) throws Exception {

		// TODO
		// RsuiteCustomizationFactory oxygenCustomization = new
		// RsuiteCustomizationFactory();

		documentURI = customizationFactory.createDocumentURI(parameters);
		this.mapMainComponent = mapMainComponent; 
		customization = DocumentCustomizationFactory.createCustomization(
				customizationFactory, documentURI, parameters);

		this.tabbedPane = tabbedPane;
		
		factory.addDITAMapTreeTargetInformationProvider("http",
				new TopicRefTargetInfoProvider() {

					@Override
					public void computeTopicRefTargetInfo(
							Map<TopicRefInfo, TopicRefTargetInfo> ditaMapTargetReferences) {
						Iterator<TopicRefInfo> keys = ditaMapTargetReferences
								.keySet().iterator();
						while (keys.hasNext()) {
							// Here you can see for what "href" the target
							// information should be resolved
							TopicRefInfo ti = keys.next();
							TopicRefTargetInfo target = ditaMapTargetReferences
									.get(ti);
							String href = (String) ti
									.getProperty(TopicRefInfo.HREF_VALUE);
							// The plugin will handle this reference
							target.setProperty(TopicRefTargetInfo.RESOLVED,
									"true");
							// And then resolve the different properties from
							// the CMS Metadata
							// The title from the target topic
							target.setProperty(TopicRefTargetInfo.TITLE, href
									+ "");
							// The class attribute of the target element name
							target.setProperty(TopicRefTargetInfo.CLASS_VALUE,
									" topic/topic ");
							// The target element name
							target.setProperty(TopicRefTargetInfo.ELEMENT_NAME,
									"topic");
							// A parse error if something happened retrieving
							// the data (maybe the target does not exist).
							target.setProperty(TopicRefTargetInfo.PARSE_ERROR,
									null);
						}
					}
				});

		ditaMapComponent = factory.createDITAMapTreeComponentProvider();

		actionsToolbar = new JToolBar();
		actionsToolbar.setLayout(new ModifiedFlowLayout(FlowLayout.LEFT));
		actionsToolbar.setFloatable(false);

		documentId = parameters
				.getParameterValue(OxygenOpenDocumentParmatersNames.DOCUMENT_URI);
		URL toLoad = new URL(documentId);

		setDocumentContent(toLoad.toString(),
				OxygenIOUtils.loadDocumentFromCMS(customization, documentURI));

		
		lookupRegister = new LookupFactoryRegister(
				customization.getLookupFactory());		

		createSampleUIControls(true, true);
		initializeNotSupportedActions();
		reconfigureActionsToolbar();
		
		final OxygenDitaMapDocument mapDocument = this;
		
		 ditaMapComponent.getDITAAccess().setPopUpMenuCustomizer(new DITAMapPopupMenuCustomizer() {
			 					
			 					public void customizePopUpMenu(Object popUp,
			 							final AuthorDocumentController ditaMapDocumentController) {
			 						JPopupMenu popupMenu = (JPopupMenu) popUp;
			 						
			 						int count = popupMenu.getComponentCount();
			 						
			 						for (int i=0; i < count; i++){
			 							Component component = popupMenu.getComponent(i);
			 							 
			 							 if ((popupMenu.getComponent(i) instanceof JMenu)){
			 								JMenu menu = (JMenu)component;
			 								
			 								String text = menu.getText();
			 								
			 								if ("Append child".equalsIgnoreCase(text)){
			 									fixInsertMenu(mapDocument, menu, Position.APPEND_CHILD);			 												 									
			 								}
			 								else if ("Insert after".equalsIgnoreCase(text)){
			 									fixInsertMenu(mapDocument, menu, Position.AFTER);			 												 									
			 								}
			 							 }
			 						}
			 					}

								private void fixInsertMenu(
										final OxygenDitaMapDocument mapDocument,
										JMenu menu, Position position) {
									Component[] components =  menu.getMenuComponents();
									
									LinkedHashMap<String, Action> appendAction = new LinkedHashMap<String, Action>();
											
									
									for (Component menuComponent : components){
										if (menuComponent instanceof JMenuItem){
											JMenuItem menuItem = (JMenuItem)menuComponent;
											
											
											
											String actionName = menuItem.getAction().getValue(Action.NAME).toString();  
											
											if (actionName.equals("Topic group...") || actionName.equals("Topic heading...")){
												appendAction.put(actionName, menuItem.getAction());
											}			 														 											
										}
									}
									
									menu.removeAll();
									
									
									
									menu.add(new InsertMapReferenceAction(mapDocument, position));
									
									for (Action action : appendAction.values()){
										menu.add(action);
									}
								}
			 				});

	}
	
	private void initializeNotSupportedActions(){
		notSupportedActions.add("DITA_Maps_Manager/Open_map_in_editor");
		notSupportedActions.add("DITA_Maps_Manager/Open_map_in_editor_with_resolved_topics");
		notSupportedActions.add("DITA_Maps_Manager/Refresh_references");
		notSupportedActions.add("DITA_Maps_Manager/Open");
		notSupportedActions.add("DITA_Maps_Manager/Paste_after");
		notSupportedActions.add("DITA_Maps_Manager/Paste_before");
		notSupportedActions.add("DITA_Maps_Manager/Edit_Profiling_Attributes");
		
	}

	/**
	 * Check if the text from the editor was modified.
	 * 
	 * @return true if it was modified.
	 */
	public boolean isModified() {
		return ditaMapComponent.isModified();
	}

	/**
	 * Public method, used from the JavaScript. It uses the AWT thread to do the
	 * real execution, since the security manager will not allow operations on
	 * the browser thread.
	 * 
	 * @param url
	 *            The URL of the file to load, can be null if XML content is
	 *            specified. If no XML content is given, the URL will be used
	 *            both to obtain the content and to solve relative references
	 *            (eg: images). If the XML content is also given, the URL will
	 *            only be used to solve relative references from the file.
	 * 
	 * @param xmlContent
	 *            The xml content.
	 * @throws Exception
	 */
	public void setDocumentContent(final String url, final String xmlContent)
			throws OxygenIntegrationException {
		final Exception[] recorded = new Exception[1];
		OxygenUtils.invokeOnAWT(new Runnable() {
			public void run() {
				try {
					setDocument(url, xmlContent != null
							&& xmlContent.length() > 0 ? new StringReader(
							xmlContent) : null);
				} catch (Exception ex) {
					recorded[0] = ex;
				}
			}
		});
		if (recorded[0] != null) {
			throw new OxygenIntegrationException(recorded[0]);
		}
	}

	/**
	 * Set a new content
	 * 
	 * @param xmlSystemId
	 *            The system ID (URL) of the XML content, used to solve images,
	 *            etc
	 * @param xmlContent
	 *            The reader over the content.
	 * @throws AuthorComponentException
	 */
	public void setDocument(final String xmlSystemId, final Reader xmlContent)
			throws AuthorComponentException {
		final Exception[] ex = new Exception[1];
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				// Installs a document modification listener.
				try {
					ditaMapComponent
							.load((xmlSystemId != null && xmlSystemId.trim()
									.length() > 0) ? new URL(xmlSystemId)
									: null, xmlContent);
				} catch (Exception e) {
					ex[0] = e;
				}
				return null;
			}
		});
		if (ex[0] != null) {
			throw new AuthorComponentException(ex[0]);
		}
	};

	/**
	 * Get the content serialized back to XML from the component
	 * 
	 * @return The content serialized back to XML from the component
	 */
	public String getSerializedDocument() {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {
			public String run() {
				return OxygenIOUtils.getSerializedDocument(ditaMapComponent,
						customization);
			}
		});
	}

	
	/**
	 * Reconfigure the actions toolbar
	 */
	public void reconfigureActionsToolbar() {
		actionsToolbar.removeAll();
		Map<String, AbstractAction> authorCommonActions = ditaMapComponent
				.getDITACommonActions();
		
		addSystemCustomButtons();
		
		// Author extensions
		Iterator<String> iter = authorCommonActions.keySet().iterator();
		while (iter.hasNext()) {
			String actionID = iter.next();
			AbstractAction action = authorCommonActions.get(actionID);
			
			
			if (action.getValue(Action.SMALL_ICON) != null && !notSupportedActions.contains(actionID)) {
				
				if ("DITA_Maps_Manager/Insert_Reference".equalsIgnoreCase(actionID)){
					action = new InsertMapReferenceAction("Insert topicref", (Icon)action.getValue(Action.SMALL_ICON), this);
					actionsToolbar.add(new OxygenButton(action));
				}else{
					actionsToolbar.add(new OxygenButton(action));
				}
				
				
			}
		}

		if (actionsToolbar.getParent() != null) {
			actionsToolbar.getParent().invalidate();
			actionsToolbar.revalidate();
		}
	}

	/**
	 * Creates a sample editor panel.
	 * 
	 * @param addStatus
	 *            Add the status bar
	 * @param addToolbar
	 *            True to add a toolbar
	 * @return The sample panel
	 */
	private void createSampleUIControls(boolean addStatus, boolean addToolbar) {
		// JPanel samplePanel = new JPanel(new BorderLayout());
		// if (addStatus) {
		// // Add status in south
		// samplePanel.add(ditaMapComponent.getStatusComponent(),
		// BorderLayout.SOUTH);
		// }
		// if (addToolbar) {
		// // Add toolbar in north
		// samplePanel.add(actionsT	oolbar, BorderLayout.NORTH);
		// }
		//
		// samplePanel.add(ditaMapComponent.getEditorComponent(),
		// BorderLayout.CENTER);

		// Reconfigures the toolbar when the document type detection changes.
		
		
		
		ditaMapComponent
				.addDITAMapTreeComponentListener(new DITAMapTreeComponentListener() {
					public void modifiedStateChanged(boolean modified) {
						if (modified && !isModified) {
							setModifiedTitle();
							//tabbedPane.repaint();
						}

						isModified = modified;

					}

					/**
					 * Reconfigures the actions toolbar
					 * 
					 * @see ro.sync.ecss.extensions.api.component.listeners.AuthorComponentListener#documentTypeChanged()
					 */
					public void documentTypeChanged() {
						reconfigureActionsToolbar();
					}

					/**
					 * Reconfigures the actions toolbar
					 * 
					 * @see ro.sync.ecss.extensions.api.component.listeners.AuthorComponentListener#loadedDocumentChanged()
					 */
					public void loadedDocumentChanged() {
						reconfigureActionsToolbar();
					}
				});
		// return samplePanel;
	}

	public DITAMapTreeComponentProvider getDitaMapComponent() {
		return ditaMapComponent;
	}

	public JToolBar getActionsToolbar() {
		return actionsToolbar;
	}

	@Override
	public boolean equals(Object obj) {

		
		if (obj instanceof IOxygenDocument) {

			String documentIdToCompare = ((IOxygenDocument) obj)
					.getDocumentUri().getDocumentURI();
			
			if (documentIdToCompare == null && documentId == null) {
				return true;
			}

			if (documentId != null) {
				return documentId.equals(documentIdToCompare);
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash
				+ (this.documentId != null ? this.documentId.hashCode() : 0);
		return hash;
	}

	@Override
	public IDocumentURI getDocumentUri() {
		return documentURI;
	}

	@Override
	public void restoreFrameTitle() {
//		int index = OxygenMainComponent.getTabIndex(this);
//		String title = tabbedPane.getTitleAt(index);
//		tabbedPane.setTitleAt(index, title.substring(0, title.length() - 2));
		
		
		ditaMapComponent.getWSEditorAccess().setModified(false);
		isModified = false;
		mapMainComponent.getSystemButtons().changeButtonStatus(OxygenSystemToolbarButtons.BUTTON_SAVE, false);
		
	}

	@Override
	public IDocumentCustomization getDocumentCustomization() {
		return null;
	}
	
	private void addSystemCustomButtons() {

		OxygenMapManagerSystemToolbarButtons systemButtons = mapMainComponent.getSystemButtons();
		List<JComponent> buttonsList = systemButtons.getSystemButtons();
		
		for (JComponent button : buttonsList){
			actionsToolbar.add(button);
		}
		systemButtons.changeButtonStatus(RSuiteOxygenConstants.BUTTON_SAVE, isModified);
				
	}
	
	public void setModifiedTitle() {
		int index = OxygenDitaMapsMainComponent.getTabIndex(this);
		String title = tabbedPane.getTitleAt(index);		
//		IOxygenComponentToolbar actionsToolbar = documentViews
//				.getComponentToolbar();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				mapMainComponent.getSystemButtons().changeButtonStatus(OxygenSystemToolbarButtons.BUTTON_SAVE, true);
			}
		});
		this.isModified = true;
		ditaMapComponent.getWSEditorAccess().setModified(false);
		tabbedPane.setTitleAt(index, title + " *");

		actionsToolbar.revalidate();
	}
	
	public LookupFactoryRegister getLookupRegister() {
		return lookupRegister;
	}

	@Override
	public AuthorDocumentController getOxygenDocumentController() {
		return ditaMapComponent.getDITAAccess().getDocumentController();
	}
}
