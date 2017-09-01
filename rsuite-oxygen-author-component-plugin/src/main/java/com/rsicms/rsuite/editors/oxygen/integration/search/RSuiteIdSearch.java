package com.rsicms.rsuite.editors.oxygen.integration.search;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.search.result.MoResult;
import com.rsicms.rsuite.helpers.utils.MoUtils;

public class RSuiteIdSearch {

	private static Log logger = LogFactory.getLog(RSuiteIdSearch.class);

	public static boolean isRSuiteIdSearch(String phrase) {
		if (phrase != null && phrase.trim().matches("^id:[0-9]+")) {
			return true;
		}

		return false;
	}

	public static Collection<MoResult> rsuiteIdSearch(ExecutionContext context,
			String phrase) {

		Collection<MoResult> result = new ArrayList<>();

		String rsuiteId = phrase.trim().substring(3);

		try {
			ManagedObject managedObject = MoUtils.getRealMo(context, rsuiteId);
			if (managedObject != null && !managedObject.isAssemblyNode()) {
				result.add(new MoResult(context, managedObject));
			}
		} catch (RSuiteException e) {
			logger.error(e, e);
		}

		return result;
	}

}
