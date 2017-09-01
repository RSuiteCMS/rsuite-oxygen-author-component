package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ro.sync.ecss.extensions.api.AuthorAccess;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenComponentRegister;
import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenEditorContext;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICustomizationFactory;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.IDocumentURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReferenceTargetLookup;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.linktarget.LinkElementItem;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.linktarget.ListElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.references.InsertReferenceElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.UriUtils;

public class RsuiteTargetLookupImpl implements IReferenceTargetLookup {

	private IDocumentURI documentUri;
	
	private ICmsURI cmsURI;

	// TODO make default implementation
	private String lookupPath;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public RsuiteTargetLookupImpl(IDocumentURI documentUri) {
		this.cmsURI = documentUri.getCMSUri(); 
		lookupPath = cmsURI.getLookUpUri();
		this.documentUri = documentUri;

	}

	private List<IReferenceTargetElement> sendRequest(AuthorAccess authorAccess,
			String cmsId, InsertReferenceElement element) throws Exception {

		String lookupPath = element.getTargetNodeWS();

		if (StringUtils.isEmpty(lookupPath)) {
			lookupPath = this.lookupPath;
		}

		String href = UriUtils.addParameterToUri(lookupPath, "id=" + cmsId
				+ "&" + cmsURI.getSessionKeyParam());
		
		href += "&" + sdf.format(new Date());
		
		URL url = new URL(href);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);

		httpCon.setRequestProperty("Content-Type",
				"text/xml; charset=UTF-8");
		httpCon.setRequestMethod("GET");
		

		
		InputStream is = httpCon.getInputStream();				
		List<IReferenceTargetElement> children = parseResponse(is);
		httpCon.disconnect();

		return children;
	}

	private List<IReferenceTargetElement> parseResponse(InputStream is)
			throws Exception {
		List<IReferenceTargetElement> nodes = new ArrayList<IReferenceTargetElement>();
		JAXBContext context = JAXBContext.newInstance(ListElement.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
		ListElement listElement = (ListElement) unmarshaller.unmarshal(isReader);

		for (LinkElementItem elementItem : listElement.getTargetElementList()) {
			//TODO to remove
			elementItem.setEditedResourceId(documentUri.getEditedDocumentId());
			nodes.add(elementItem);
		}

		return nodes;
	}

	@Override
	public List<IReferenceTargetElement> getReferenceTargetElementList(
			AuthorAccess authorAccess, IReposiotryResource userNode,
			InsertReferenceElement element) throws Exception {

		String cmsId = userNode.getCMSid();
		
		if (cmsId.equalsIgnoreCase(documentUri.getEditedDocumentId())) {
			return getLocalReferenceTargetElementList(authorAccess, cmsId, element);
		}

		return sendRequest(authorAccess, cmsId, element);
	}

	public List<IReferenceTargetElement> getLocalReferenceTargetElementList(
			AuthorAccess authorAccess, String cmsId,
			InsertReferenceElement element) throws Exception {

		if (element.getTargetNodeListStylesheet() != null) {
			return parseResponse(transformLocalDocument(authorAccess, element));
		}

		return sendRequest(authorAccess, cmsId, element);

	}

	private InputStream transformLocalDocument(AuthorAccess authorAccess,
			InsertReferenceElement element) throws Exception {
		OxygenEditorContext context = OxygenComponentRegister
				.getRegisterOxygenEditorContext(authorAccess);

		
		ICustomizationFactory customizationFactory = context.getMainComponent().getCustomizationFactory();
		

		Reader reader = context.getMainComponent().getActiveDocumentComponent().getEditedDocument();
		InputStream stylesheetIS = IOUtils.toInputStream(element.getTargetNodeListStylesheet());

		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setEntityResolver(customizationFactory.getEntityResolver());

		TransformerFactory factory = TransformerFactory.newInstance();

		Source source = new StreamSource(stylesheetIS);
		Transformer transformer = factory.newTransformer(source);

		InputSource inputSource = new InputSource(reader);

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		// XMLReader xmlReader = new XmlRead
		Result result = new StreamResult(os);
		SAXSource xml = new SAXSource(xmlReader, inputSource);

		transformer.transform(xml, result);

		return new ByteArrayInputStream(os.toByteArray());
	}

}
