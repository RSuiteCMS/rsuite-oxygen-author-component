package com.rsicms.rsuite.editors.oxygen.integration.search.result;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.api.repository.Repository;

public class MoResultFetcher {

	private Repository repository;

	public MoResultFetcher(Repository repository) {
		this.repository = repository;
	}

	public Collection<MoResult> obtainMoResults(List<String> moIds)
			throws IOException, RSuiteException {

		List<MoResult> resultList = new ArrayList<MoResult>();

		try {

			String moResulQuery = createMoResultQuery(moIds);

			Map<String, String> parameters = new HashMap<String, String>();

			ComposedXQuery composedQuery = repository.getQueryBuilder()
					.composeQuery(moResulQuery, parameters);

			String[] results = repository.queryAsStringArray(composedQuery);

			resultList = MoResultParser.parseResults(results);

		} catch (XMLStreamException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		} catch (ParseException e) {
			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}

		return resultList;
	}

	private String createMoResultQuery(List<String> moIds) throws IOException {
		String query = IOUtils.toString(
				this.getClass().getResourceAsStream(
						"/WebContent/xquery/moResult.xqy"), "utf-8");

		String ids = convertMoIdListToSequence(moIds);

		query = query.replace("$resourceIDs$", ids);
		return query;
	}

	private String convertMoIdListToSequence(List<String> moIds) {

		StringBuilder sequence = new StringBuilder();

		for (Iterator<String> iterator = moIds.iterator(); iterator.hasNext();) {
			String string = iterator.next();

			sequence.append("\"").append(string).append("\" ");

			if (iterator.hasNext()) {
				sequence.append(", ");
			}

		}

		return sequence.toString();
	}

}
