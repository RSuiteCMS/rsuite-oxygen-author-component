package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.ITreeOxygenLookUp;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.tree.connector.jaxb.repository.TreeElement;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.UriUtils;

public class RsuiteOxygenLookUp implements ITreeOxygenLookUp {

	private ICmsURI cmsURI;

	private String lookupPath;
	
	private String parameters  = "";
	
	private String rootId = "0";

	public RsuiteOxygenLookUp(ICmsURI cmsUri) {
		this.cmsURI = cmsUri;
		lookupPath = cmsURI.getLookUpUri();
		
	}
	
	public RsuiteOxygenLookUp(ICmsURI cmsUri, String parameters, String rootId) {
		this(cmsUri);
		this.rootId = rootId;
		this.parameters = parameters;
	}

	private List<IReposiotryResource> sendRequest(String href) throws Exception {
		URL url = new URL(UriUtils.addParameterToUri(href,cmsURI.getSessionKeyParam()));		
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);

		httpCon.setRequestMethod("GET");
		httpCon.connect();
		InputStream is = httpCon.getInputStream();
		List<IReposiotryResource> children = parseResponse(is);
		httpCon.disconnect();

		return children;
	}

	private List<IReposiotryResource> parseResponse(InputStream is)
			throws Exception {
		List<IReposiotryResource> nodes = new ArrayList<IReposiotryResource>();
		JAXBContext context = JAXBContext.newInstance(TreeElement.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		
		TreeElement user = (TreeElement) unmarshaller.unmarshal(is);
		nodes.addAll(user.getInfoList());
		return nodes;
	}

	@Override
	public List<IReposiotryResource> getRootChildren() throws Exception {
		return getChildren(rootId);
	}

	@Override
	public List<IReposiotryResource> getChildren(String id) throws Exception {	
		String href = UriUtils.addParameterToUri(lookupPath, "id=" + id);
		href = UriUtils.addParameterToUri(href, parameters);
		
		return sendRequest(href);
	}

	@Override
	public String getRootDisplayText() {
		return "Repository";
	}

	@Override
	public String getRootIcon() {		
		return "images/folder.png";
	}

}
