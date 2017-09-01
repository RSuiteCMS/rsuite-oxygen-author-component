package com.rsicms.rsuite.editors.oxygen.integration.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.repository.ComposedXQuery;
import com.reallysi.rsuite.api.repository.Repository;
import com.reallysi.rsuite.api.security.Role;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResult;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResultFetcher;

public class XMLSearch {

	private Repository repository;

	private MoResultFetcher moResultFetcher;
	
	private User user;
	
	private ExecutionContext context;

	public XMLSearch(ExecutionContext context, User user) {
		this.user = user;
		repository = context.getRepositoryService().getRepository();
		moResultFetcher = new MoResultFetcher(repository);
		this.context = context;
	}

	public Collection<MoResult> searchXml(List<String> elements, String phrase,
			boolean topMoOnly) throws RSuiteException {

		if(RSuiteIdSearch.isRSuiteIdSearch(phrase)){
			return RSuiteIdSearch.rsuiteIdSearch(context, phrase);
		}
		
		List<String> rolesList = getRoleList();

		Collection<MoResult> moResults = null;

		try {
			XMLSearchQueryBuilder queryBuilder = new XMLSearchQueryBuilder();

			String searchQuery = queryBuilder.createSearchXquery(rolesList,
					elements, phrase, topMoOnly);

			String[] moIds = executeSearchQuery(searchQuery);
			moResults = moResultFetcher.obtainMoResults(Arrays.asList(moIds));

		} catch (IOException e) {

			throw new RSuiteException(0, e.getLocalizedMessage(), e);
		}

		return moResults;

	}

	private String[] executeSearchQuery(String query) throws RSuiteException {
		Map<String, String> parameters = new HashMap<String, String>();
		ComposedXQuery composedQuery = repository.getQueryBuilder()
				.composeQuery(query, parameters);

		return repository.queryAsStringArray(composedQuery);
	}

	private List<String> getRoleList() {

		List<String> rolesList = new ArrayList<String>();

		for (Role role : user.getRoles()) {
			rolesList.add(role.getName());
		}
		return rolesList;
	}

	
}
