package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.InputStream;

import org.junit.Test;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.test.TestUtils;

public class ChangeTrackingUpdaterTest {

	@Test
	public void remove_rsuite_id_attribute_from_delete_change_tracking_processing_instruction() throws OxygenIntegrationException {
	
		InputStream documentInputStream = TestUtils.getResource(getClass(), "deleteChangeTracking.xml");
		
		ChangeTrackingUpdater ctUpdater = new ChangeTrackingUpdater();
		RSuiteDocumentParser documentParser = new RSuiteDocumentParser(ctUpdater);
		documentParser.parseRSuiteDocument(documentInputStream);

		String updatedDocument = ctUpdater.getUpdatedDocument();
		
		assertThat(updatedDocument, containsString("r:rsuiteId=\"2336\""));
		assertThat(updatedDocument, not(containsString("r:rsuiteId=&quot;2338&quot;")));		
	}

}
