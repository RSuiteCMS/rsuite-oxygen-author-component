package com.rsicms.rsuite.editors.oxygen.integration.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class XMLSearchQueryBuilder {

	private static String DEFAULT_SEARCH_LIMIT = "2000";

	private String searchLimit;

	private Pattern pattern = Pattern.compile("rsuiteLimit:([0-9]+)");
	
	public XMLSearchQueryBuilder() {
		this.searchLimit = DEFAULT_SEARCH_LIMIT;
	}
	
	public XMLSearchQueryBuilder(long searchLimit) {
		super();
		this.searchLimit = String.valueOf(searchLimit);
	}

	public String createSearchXquery(List<String> rolesList,
			List<String> elements, String phrase, boolean topResourceOnly)
			throws IOException {

		String limit = null;

		if (phrase != null) {
			Matcher matcher = pattern.matcher(phrase);

			if (matcher.find()) {
				limit = matcher.group(1);
				phrase = matcher.replaceAll("").trim();
			}
		}

		if (limit == null) {
			limit = searchLimit;
		}

		String queryTemplate = IOUtils.toString(this.getClass()
				.getResourceAsStream("/WebContent/xquery/search.xqy"), "utf-8");

		String roles = convertRoleList(rolesList);

		StringBuilder declarations = new StringBuilder();

		String xpaht = createXpath(elements, topResourceOnly, declarations);

		String searchStatement = createSearchStatement(phrase, xpaht);

		queryTemplate = queryTemplate.replace("$declarations$",
				declarations.toString());
		queryTemplate = queryTemplate.replace("$roles$", roles);
		queryTemplate = queryTemplate.replace("$searchStatement$",
				searchStatement);
		queryTemplate = queryTemplate.replace("$limit$", limit);

		return queryTemplate;
	}
	
	private String convertRoleList(List<String> rolesList) {
		StringBuilder roles = new StringBuilder(" \"*\" ");

		for (String role : rolesList) {
			roles.append(" , \"").append(role).append("\" ");
		}
		return roles.toString();
	}

	private String createSearchStatement(String phrase, String xpaht) {
		StringBuilder searchStatement = new StringBuilder();

		searchStatement.append("cts:search(");
		searchStatement.append(xpaht);
		searchStatement.append("[@r:rsuiteId], ");

		if (StringUtils.isNotBlank(phrase)) {

			searchStatement.append("cts:and-query((cts:word-query(\"");
			searchStatement.append(phrase.replaceAll("\"", "&#34;"));
			searchStatement.append("\"), ");

		}

		searchStatement.append("cts:collection-query('rsuite:mv-current'))");

		if (StringUtils.isNotBlank(phrase)) {
			searchStatement.append("))");
		}
		return searchStatement.toString();
	}

	private List<SearchXMLType> parseSearchElements(List<String> elements) {
		List<SearchXMLType> xmlTypes = new ArrayList<SearchXMLType>();

		for (String element : elements) {
			int lastColon = element.lastIndexOf(":");

			String namespaceUri = "";
			String elementName = element;

			if (lastColon > -1) {
				namespaceUri = element.substring(0, lastColon);
				elementName = elementName.substring(lastColon + 1);
			}

			xmlTypes.add(new SearchXMLType(elementName, namespaceUri));
		}
		return xmlTypes;
	}
	
	
	private String createXpath(List<String> elements, boolean topResourceOnly,
			StringBuilder declarations) {
		List<SearchXMLType> xmlTypes = parseSearchElements(elements);

		String xpaht = "/(";

		if (!topResourceOnly) {
			xpaht = "/" + xpaht;
		}

		if (xmlTypes.size() == 0) {
			xpaht += "*";
		}

		int i = 0;

		for (Iterator<SearchXMLType> iterator = xmlTypes.iterator(); iterator
				.hasNext();) {
			SearchXMLType xmlType = iterator.next();

			String elementName = xmlType.getName();
			String namespace = xmlType.getNamespaceUri();
			String namespacePrefix = "";

			if (StringUtils.isNotBlank(namespace)) {
				namespacePrefix = "ns" + i;
				declarations.append("\n");
				declarations.append("declare namespace " + namespacePrefix
						+ "=\"" + namespace + "\";");
				i++;
			}

			if (StringUtils.isNotBlank(namespace)) {
				xpaht += namespacePrefix + ":";
			}

			if (StringUtils.isNotBlank(elementName)) {
				xpaht += elementName;
			} else {
				xpaht += "*";
			}

			if (iterator.hasNext()) {
				xpaht += " | ";
			}

		}

		xpaht += ")";
		return xpaht;
	}
}
