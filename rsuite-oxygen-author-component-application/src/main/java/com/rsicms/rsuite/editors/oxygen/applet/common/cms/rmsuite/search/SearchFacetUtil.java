package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import static com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacetHelper.encode;
import static com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacetHelper.sendSearchRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonParseException;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;

public class SearchFacetUtil implements ISearchFacetUtil {

	private String skeyParam;

	private String host;

	private ICmsURI cmsURI;

	private XMLInputFactory xmlFactory = XMLInputFactory.newFactory();

	public SearchFacetUtil(ICmsURI cmsURI) {
		this.skeyParam = cmsURI.getSessionKeyParam();
		this.host = cmsURI.getHostURI();
		this.cmsURI = cmsURI;

	}

	/* (non-Javadoc)
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ISearchFacetUtil#getXmlDocTypes()
	 */
	@Override
	public Map<String, String> getXmlDocTypes() throws Exception {
		return getSearchTypes("rsuite:xmlDoc");
	}

	/* (non-Javadoc)
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ISearchFacetUtil#getMediaTypes()
	 */
	@Override
	public Map<String, String> getMediaTypes() throws Exception {
		return getSearchTypes("rsuite:docMedia");
	}

	public Map<String, String> getSearchTypes(String type) throws Exception {
		URL url = new URL(host + "/rsuite/rest/v2/search/facet/basicFacet?"
				+ skeyParam);
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();

		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();

		Map<String, String> moElements = new LinkedHashMap<String, String>();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();

		String path = "/map/facets/map[name/text() = '" + type
				+ "']/values/DataTypeOptionValue";

		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xPath.evaluate(path,
				doc.getDocumentElement(), XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); ++i) {
			Element e = (Element) nodes.item(i);

			String value = e.getElementsByTagName("value").item(0)
					.getTextContent();
			String label = e.getElementsByTagName("label").item(0)
					.getTextContent();

			moElements.put(value, label);

		}

		httpConnection.disconnect();

		return moElements;
	}

	/* (non-Javadoc)
	 * @see com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.ISearchFacetUtil#performFacetSearch(com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search.SearchFacet)
	 */
	@Override
	public List<ResultItem> performFacetSearch(SearchFacet searchFacet)
			throws Exception {
		
		if (searchFacet.isRSuiteIdSearch()){
			return performRSuiteIdSearch(searchFacet);
		}
		
		String type = "faceted";
		String start = "1";
		String end = "0";
		String query = searchFacet.generateFacetedQuery();

		RsuiteSearch search = getSearch(type, start, end, query);

		return getSearchResult(search.getSearchId(), 1, search.getResultCount());

	}
	
	private List<ResultItem> performRSuiteIdSearch(SearchFacet searchFacet) throws JsonParseException, IOException {
		
		String parameters = "&phrase=" + encode(searchFacet.getText());
		
		URL url = new URL(host
				+ "/rsuite/rest/v1/api/rsuite.oxygen.search.rsuiteId?"
				+ skeyParam + parameters);
		
		return  sendSearchRequest(cmsURI, url);
		
	}

	private List<String> parseSearchResult(InputStream is) throws Exception {

		List<String> items = new ArrayList<String>();

		XMLStreamReader reader = xmlFactory.createXMLStreamReader(is);

		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("originalId".equals(element)) {
					
					StringBuilder id = new StringBuilder();
					
					int charEvent = reader.next();
					while (charEvent == XMLStreamConstants.CHARACTERS) {
						id.append(reader.getText());
						charEvent = reader.next();
					}
					
					if (StringUtils.isNotEmpty(id.toString())){
						items.add(id.toString());
					}
				}
			}
		}

		return items;
	}

	private List<ResultItem> parseDetails(InputStream is) throws Exception {

		List<ResultItem> items = new ArrayList<ResultItem>();
		Map<String, ResultItem> resultMapItems = new HashMap<String, ResultItem>();

		XMLStreamReader reader = xmlFactory.createXMLStreamReader(is);

		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("contentDisplayObjects".equals(element)) {
					resultMapItems =  parseContentDisplayObjects(reader);
				}
				
				if ("managedObjects".equals(element)) {
					parseManagedObjects(reader, resultMapItems);					
				}
			}
		}

		items.addAll(resultMapItems.values());
		
		return items;
	}

	private Map<String, ResultItem> parseContentDisplayObjects(
			XMLStreamReader reader) throws Exception {

		Map<String, ResultItem> resultMapItems = new HashMap<String, ResultItem>();
		ResultItem resultItem = null;
		
		while (reader.hasNext()) {
			int event = reader.next();
			

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();
				
				
				if ("map".equals(element)) {
					
					String rsuiteId = reader.getAttributeValue("", "name");
					if (rsuiteId != null){
						resultItem = createResultItem(resultMapItems, rsuiteId);
					}
				}
				
				if ("customValues".equals(element) && resultItem != null) {
					parseCustomValues(reader, resultItem);
				}
			}
			
			if (event == XMLStreamConstants.END_ELEMENT
					&& "contentDisplayObjects".equals(reader.getLocalName())) {
				break;
			}
		}
		return resultMapItems;
	}

	private ResultItem createResultItem(Map<String, ResultItem> resultMapItems,
			String rsuiteId) {
		
		if (rsuiteId.contains(":")){
			rsuiteId = rsuiteId.substring(rsuiteId.indexOf(":") + 1);
		}
		
		ResultItem resultItem = new ResultItem();
		resultMapItems.put(rsuiteId, resultItem);
		return resultItem;
	}

	private static void parseManagedObjects(XMLStreamReader reader, Map<String, ResultItem> resultMapItems) throws Exception {
		

		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();
				String rsuiteId = reader.getAttributeValue("", "name");
				
				if ("map".equals(element) && (rsuiteId) != null) {
					ResultItem resultItem = resultMapItems.get(rsuiteId);
					if (resultItem != null){
						parseManagedObject(reader, resultItem);
					}
				}			
			}

			if (event == XMLStreamConstants.END_ELEMENT
					&& "managedObjects".equals(reader.getLocalName())) {
				break;
			}
		}

	}

	private static void parseManagedObject(XMLStreamReader reader, ResultItem resultItem)
			throws Exception {

		int mapElementCount = 1;
		
		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("map".equals(element)){
					mapElementCount++;
				}
				
				if ("versionHistory".equals(element)
						|| "metadata".equals(element)) {
					skipElement(reader, element);
				}

				if ("id".equals(element)) {
					resultItem.setRsuiteId(reader.getElementText());
				}

				if ("dtModified".equals(element)) {
					resultItem.setDateModified(reader.getAttributeValue("",
							"unix"));
				}

				if ("dtCreated".equals(element)) {
					resultItem.setDateCreated(reader.getAttributeValue("",
							"unix"));
				}

				if ("displayName".equals(element)) {
					resultItem.setDisplayName(reader.getElementText());

				}

				if ("aliases".equals(element)) {
					resultItem.setAliases(parseAliases(reader));
				}

				if ("localName".equals(element)) {
					resultItem.setLocalName(reader.getElementText());
				}

				if ("parent".equals(element)) {
					resultItem.setParentId(reader.getElementText());
				}

				if ("objectType".equals(element)) {
					String value = reader.getElementText();

					if ("mononxml".equalsIgnoreCase(value)) {
						resultItem.setXml(false);
					} else {
						resultItem.setXml(true);
					}

				}

			}

			if (event == XMLStreamConstants.END_ELEMENT					
					&& "map".equals(reader.getLocalName())) {
				
				mapElementCount--;
				
				if (mapElementCount == 0){					
					break;
				}
				
			}
		}

	}

	public static Map<String, String> parseAliases(XMLStreamReader reader)
			throws Exception {

		Map<String, String> aliases = new LinkedHashMap<String, String>();
		String alias = null;
		String type = null;

		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("text".equals(element)) {
					alias = reader.getElementText();
				}

				if ("type".equals(element)) {
					type = reader.getElementText();
				}

			}

			if (event == XMLStreamConstants.END_ELEMENT
					&& "map".equals(reader.getLocalName()) && alias != null
					&& type != null) {
				aliases.put(alias, type);
				alias = null;
				type = null;
			}

			if (event == XMLStreamConstants.END_ELEMENT
					&& "aliases".equals(reader.getLocalName())) {
				break;
			}
		}

		return aliases;
	}

	private static void parseCustomValues(XMLStreamReader reader,
			ResultItem resultItem) throws Exception {
		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("kind".equals(element)) {
					resultItem.setKind(reader.getElementText());
				}

			}
			if (event == XMLStreamConstants.END_ELEMENT
					&& "customValues".equals(reader.getLocalName())) {
				return;
			}
		}
	}

	private static void skipElement(XMLStreamReader reader, String elementName)
			throws Exception {
		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.END_ELEMENT
					&& elementName.equals(reader.getLocalName())) {
				return;
			}
		}
	}

	

	private RsuiteSearch getSearch(String type, String start, String end,
			String query) throws Exception {
		ClientResource itemsResource = new ClientResource(host
				+ "/rsuite/rest/v2/search?" + skeyParam);
		Form form = new Form();

		form.add("rs-type", type);
		form.add("rs-start", start);
		form.add("rs-end", end);
		form.add("rs-query", query);

		Representation r = itemsResource.post(form.getWebRepresentation());
		return parseSearch(r.getText());
	}

	private static RsuiteSearch parseSearch(String search) throws Exception {
		
		XMLInputFactory xmlFactory = XMLInputFactory.newFactory();
		StringReader sr = new StringReader(search);
		XMLStreamReader reader = xmlFactory.createXMLStreamReader(sr);

		String searchId = null;
		int resultCount = -1;

		while (reader.hasNext()) {
			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {
				String element = reader.getLocalName();

				if ("searchId".equals(element)) {
					searchId = reader.getElementText();
					continue;
				}

				if ("total".equals(element)) {
					for (int i = 0; i < reader.getAttributeCount(); i++) {
						String attributeName = reader.getAttributeLocalName(i);
						if ("value".equals(attributeName)) {
							resultCount = Integer.parseInt(reader
									.getAttributeValue(i));
						}
					}

				}
			}
		}

		sr.close();
		return new RsuiteSearch(searchId, resultCount);
	}

	private List<ResultItem> getSearchResult(String searchId, int start, int end)
			throws Exception {

		List<ResultItem> resultItems = new ArrayList<ResultItem>();

		String params = "&rs-searchId=" + searchId + "&rs-start=" + start
				+ "&rs-end=" + end;

		URL url = new URL(host + "/rsuite/rest/v2/search?"

		+ skeyParam + params);

		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();
		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();
		
		List<String> searchResults = parseSearchResult(is);
		httpConnection.disconnect();
		
		resultItems = getContentDetails(searchResults);

		for (ResultItem item : resultItems) {
			item.setCmsURI(cmsURI);
		}

		return resultItems;
	}

	private List<ResultItem> getContentDetails(List<String> contentIdsList) throws Exception{
		int MAX_REQUEST_LENGTH = 8192;
		
		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		StringBuilder requestURL = createInitialContentRequestURL();
		
		for (Iterator<String> iter = contentIdsList.iterator(); iter.hasNext(); ) {
			String contentId = iter.next();
			requestURL.append("&id=").append(contentId);
			
			if (requestURL.length() > MAX_REQUEST_LENGTH || iter.hasNext() == false){
				resultItems.addAll(sendContentDetailsRequest(requestURL.toString()));
				requestURL = createInitialContentRequestURL();
			}
		}

		return resultItems;
	}

	private StringBuilder createInitialContentRequestURL() {
		return new StringBuilder(host + "/rsuite/rest/v2/content?"

		+ skeyParam);
	}
	
	private  List<ResultItem> sendContentDetailsRequest(String requestURL) throws Exception {
	
		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		URL url = new URL(requestURL);
		
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();
		httpConnection.connect();
		InputStream is = httpConnection.getInputStream();
		
		resultItems = parseDetails(is);
		httpConnection.disconnect();
		
		return resultItems;
		
	}

}
