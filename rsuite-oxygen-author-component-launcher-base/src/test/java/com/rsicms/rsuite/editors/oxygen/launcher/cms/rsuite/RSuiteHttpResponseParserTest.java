package com.rsicms.rsuite.editors.oxygen.launcher.cms.rsuite;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.rsicms.rsuite.editors.oxygen.launcher.common.utils.io.IOUtils;
import com.rsicms.rsuite.test.TestUtils;

public class RSuiteHttpResponseParserTest {

	@Test
	public void should_throw_exception_when_login_failed_for_rsuite41()
			throws Exception {
		InputStream is = TestUtils.getResource(getClass(),
				"loginFailResponseRSuite41.txt");
		String responseText = IOUtils.toString(is, "utf-8");

		try {
			RSuiteHttpResponseParser.parseResponseSaveResponse(responseText,
					"key");
			Assert.fail("An exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains(
					"Unknown user ID/password combination"));
		}
		
		
	}

	@Test
	public void should_throw_exception_when_login_failed_for_rsuite40()
			throws Exception {
		InputStream is = TestUtils.getResource(getClass(),
				"loginFailResponseRSuite40.txt");
		String responseText = IOUtils.toString(is, "utf-8");
		try {
			RSuiteHttpResponseParser.parseResponseSaveResponse(responseText,
					"key");
			Assert.fail("An exception should be thrown");
		} catch (Exception e) {
			Assert.assertTrue(e.getMessage().contains(
					"Unknown user ID/password combination"));
		}
	}

}
