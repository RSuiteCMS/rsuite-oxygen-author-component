package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.test.TestUtils;

public class RSuiteIdToXpathMapperTest {

	@Test
	public void collect_rsuiteId_from_document() throws OxygenIntegrationException {
		InputStream documentInputStream = TestUtils.getResource(getClass(), "rsuiteIdMapping.xml");
		
		RSuiteIdToXpathMapper rsuiteIdMapper = new RSuiteIdToXpathMapper();
		RSuiteDocumentParser documentParser = new RSuiteDocumentParser(rsuiteIdMapper);
		documentParser.parseRSuiteDocument(documentInputStream);

		Map<String, String> rsuiteIdMapping = rsuiteIdMapper.getRsuiteIDsToXpathMap();
		
		assertThat(rsuiteIdMapping, hasEntry("2336", "/*[1]"));
		assertThat(rsuiteIdMapping, hasEntry("2340", "/*[1]/*[2]"));
		assertThat(rsuiteIdMapping, hasEntry("2345", "/*[1]/*[4]/*[2]/*[1]"));
		
	}

}
