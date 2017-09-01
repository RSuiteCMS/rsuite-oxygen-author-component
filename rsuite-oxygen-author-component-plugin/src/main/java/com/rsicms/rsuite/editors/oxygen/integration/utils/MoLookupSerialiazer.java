package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.Alias;
import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.ILookupHandler;

public class MoLookupSerialiazer {

	private ILookupHandler lookupHandler;

	public MoLookupSerialiazer(ILookupHandler lookupHandler) {
		super();
		this.lookupHandler = lookupHandler;
	}

	/**
	 * Writes dhtmlx item tree element
	 * 
	 * @param xmlWriter
	 * @param objectType
	 * @param id
	 * @param name
	 * @param hasChilds
	 * @throws XMLStreamException
	 * @throws RSuiteException
	 */
	public void writeItemElement(RemoteApiExecutionContext context,
			XMLStreamWriter xmlWriter, ManagedObject mo)
			throws XMLStreamException, RSuiteException {

		ManagedObjectService mosvc = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();
		User currentUSer = context.getSession().getUser();

		ContentAssemblyItem caItem = null;

		String rsuiteReferenceId = getReferenceId(mo);
		
		mo = MoUtils.getRealMo(context, mo.getId());
		
		String rsuiteId = mo.getTargetId() == null ? mo.getId() : mo
				.getTargetId();
		

		boolean hasChilds = mosvc.hasChildren(user, rsuiteId);
		
		String name = mo.getDisplayName();
		
		if (MoUtils.isContentAssembly(mo)){
			caItem = MoUtils.getContentAssemblyItemFromMo(context, mo);
			if (caItem.isDynamic()){
				hasChilds = true;
			}
		}

	    if (name == null){
            name = "<"  + mo.getElement().getNodeName() + ">";          
        }
		

		xmlWriter.writeStartElement("item");
		xmlWriter.writeAttribute("text", name + " [" + rsuiteId + "]");

		xmlWriter.writeAttribute("id", mo.getId());

		if (hasChilds) {
			xmlWriter.writeAttribute("child", "1");
		}

		writeImageAttributes(context, xmlWriter, mo, caItem);

		writeUserData(xmlWriter, "rsuiteId", rsuiteId);
		writeUserData(xmlWriter, "displayName", name);
		
		if (mo.isCheckedoutButNotByUser(currentUSer)){
			writeUserData(xmlWriter, "checkedOutBy", mo.getCheckedOutUser());
		}
		
		if (StringUtils.isNotEmpty(rsuiteReferenceId)){
			writeUserData(xmlWriter, "rsuiteReferenceId", rsuiteReferenceId);	
		}		

		if (mo != null) {
			StringBuilder aliasesList = new StringBuilder();
			try {
				Alias[] aliases = mo.getAliases();
				for (int i = 0; i < aliases.length; i++) {
					Alias alias = aliases[i];
					aliasesList.append(alias.getText());
					if (i < aliases.length - 1) {
						aliasesList.append(",");
					}
				}
			} catch (RSuiteException e) {
				throw new RSuiteException(0,"Problem with aliases", e);
			}

			if (aliasesList.length() > 0) {
				writeUserData(xmlWriter, "aliases", aliasesList.toString());
			}

			if (lookupHandler != null) {
				Map<String, String> additionalMetadata = lookupHandler
						.getAdditionalUserMetaData(context, mo);

				if (additionalMetadata != null) {
					for (String metadata : additionalMetadata.keySet()) {
						writeUserData(xmlWriter, metadata,
								additionalMetadata.get(metadata));
					}
				}
			}
		}

		xmlWriter.writeEndElement();
	}
	
	private String getReferenceId(ManagedObject mo) throws RSuiteException {
		String refenceId = null;
		
		if (StringUtils.isNotEmpty(mo.getTargetId())){
			refenceId = mo.getId();
		}
		
		return refenceId;
	}

	public void writeImageAttributes(ExecutionContext context, XMLStreamWriter xmlWriter,
			ManagedObject mo, ContentAssemblyItem caItem) throws XMLStreamException,
			RSuiteException {

		String iconName = "";
		ObjectType objectType = mo.getObjectType();
		
		if (caItem != null) {

			
			iconName = "container.gif";
			
			if ("folder".equalsIgnoreCase(caItem.getType())) {
				iconName = "folder.png";
			}
			
			if (caItem.isDynamic()){
				iconName = "smart-folder.png";
			}
			

			if (lookupHandler != null) {
				String newIcon = lookupHandler.getContainerIconName(caItem,
						iconName);
				if (StringUtils.isNotBlank(newIcon)) {
					iconName = newIcon;
				}
			}

		} else if (objectType == ObjectType.MANAGED_OBJECT) {

			iconName = "xml.gif";
			if (lookupHandler != null) {
				String newIcon = lookupHandler.getMoIconName(mo, iconName);
				if (StringUtils.isNotBlank(newIcon)) {
					iconName = newIcon;
				}
			}

		} else if (objectType == ObjectType.MANAGED_OBJECT_NONXML) {

			iconName = "image.png";

			if (lookupHandler != null) {
				String newIcon = lookupHandler
						.getNonXmlMoIconName(mo, iconName);
				if (StringUtils.isNotBlank(newIcon)) {
					iconName = newIcon;
				}
			}

		}

		iconName = "images/" + iconName;
		xmlWriter.writeAttribute("im0", iconName);
		xmlWriter.writeAttribute("im1", iconName);
		xmlWriter.writeAttribute("im2", iconName);

		xmlWriter.writeAttribute("type", objectType.toString());

	}

	public void writeUserData(XMLStreamWriter xmlWriter, String name,
			String value) throws XMLStreamException {
		xmlWriter.writeStartElement("userdata");
		xmlWriter.writeAttribute("name", name);
		xmlWriter.writeCharacters(value);
		xmlWriter.writeEndElement();
	}

	public static void writeElementWithValue(XMLStreamWriter xmlWriter,
			String elementName, String elementValue) throws XMLStreamException {
		xmlWriter.writeStartElement(elementName);
		xmlWriter.writeCharacters(elementValue == null ? "" : elementValue);
		xmlWriter.writeEndElement();
	}
}
