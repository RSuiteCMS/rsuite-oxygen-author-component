package com.rsicms.rsuite.editors.oxygen.integration.webservice;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.Plugin;
import com.reallysi.rsuite.api.extensions.PluginAware;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.DefaultRemoteApiHandler;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.api.remoteapi.RemoteApiResult;
import com.reallysi.rsuite.api.remoteapi.result.HtmlPageResult;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.OxygenLaunchMethod;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.appletclassic.ClassicAppletGenerator;
import com.rsicms.rsuite.editors.oxygen.integration.utils.OxygenLaunchHelper;
import com.rsicms.rsuite.editors.oxygen.integration.webservice.lookup.ALookupWebService;

public class OxygenAppletContainerWebService extends DefaultRemoteApiHandler implements PluginAware {

	private Log logger = LogFactory.getLog(getClass());
	
	private Plugin plugin = null;
	
	private String APPLET_PATH = "editor/applet-html-page.xsl";
	
	private String TRANSFORM_PARAM_RSUITE_SESSIONKEY = "rsuite.sessionkey";
	
	@Override
	public RemoteApiResult execute(RemoteApiExecutionContext context, CallArgumentList args) throws RSuiteException {
		// Really cursory stuff here.
		try {
			
			OxygenLaunchHelper launchHelper = new OxygenLaunchHelper(context, args);
			
			String htmlXsl = "rsuite:/res/plugin/" + getStaticWebServicePath() + "/" + APPLET_PATH;
			
			Transformer xf = context.getXmlApiManager().getTransformer(new URI(htmlXsl));
			xf.setParameter(TRANSFORM_PARAM_RSUITE_SESSIONKEY, context.getSession().getKey());
			xf.setParameter("rsuite.managedObjectId", args.getManagedObject(context.getSession().getUser(), 0).getId());
			xf.setParameter("rsuite.pluginPath", "/rsuite-cms/plugin/" + getStaticWebServicePath());
			xf.setParameter("rsuite.appletContext", args.getFirstString("appletContext"));
			xf.setParameter("rsuite.uuid", UUID.randomUUID().toString());			
			
			MoToLaunch moToLunch  = launchHelper.getMoToLaunch();
			IOxygenIntegrationAdvisor advisor = launchHelper.initializeAndGetAdvisor(moToLunch.getManagedObject());
			
			ClassicAppletGenerator appletTagGenerator = new ClassicAppletGenerator(context);
			String appletTag = appletTagGenerator.generateAppletTag(launchHelper.getOxygenWebEditingContext(), advisor, moToLunch);
			
			ALookupWebService.registerLookupHandler(advisor.getLookupHandler());
			xf.setParameter("rsuite.applet.tag", appletTag);
			
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos);
			Reader reader = new StringReader("<xml />");
			StreamSource input = new StreamSource(reader);
			xf.transform(input, result);
			HtmlPageResult hpr = new HtmlPageResult(baos.toByteArray(), "UTF-8");
			hpr.setContentType("text/html");
			return hpr;
		} catch (Exception e) {
			logger.error(e, e);
			return new HtmlPageResult("<html><body>" + e.getLocalizedMessage() + "<body/></html>");
		}
	}

	@Override
	public void setPlugin(Plugin paramPlugin) {
		plugin = paramPlugin;
	}
	public String getStaticWebServicePath() throws RSuiteException {
		return plugin.getId();
	}
}
