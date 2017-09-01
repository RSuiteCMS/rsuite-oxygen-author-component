package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.remoteapi.CallArgumentList;
import com.reallysi.rsuite.api.remoteapi.RemoteApiExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.advisor.OxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;
import com.rsicms.rsuite.editors.oxygen.integration.api.configuration.OpenSubMoPolicy;
import com.rsicms.rsuite.editors.oxygen.integration.api.configuration.OxygenConfigurationUtils;
import com.rsicms.rsuite.editors.oxygen.integration.api.configuration.SubMoPolicy;
import com.rsicms.rsuite.editors.oxygen.integration.domain.MoToLaunch;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.FindOxygenAdvisorResult;
import com.rsicms.rsuite.editors.oxygen.integration.domain.launch.OxygenIntegrationAdvisorFactory;

public class OxygenLaunchHelper {

	private OxygenWebEditingContext oxygenWebEditingContext;

	private String rsuiteId;

	private RemoteApiExecutionContext context;
	
	private IOxygenIntegrationAdvisor advisor;

	public OxygenLaunchHelper(RemoteApiExecutionContext context,
			CallArgumentList args) throws RSuiteException {

		rsuiteId = args.getFirstValue("sourceId");
		
		if (StringUtils.isBlank(rsuiteId)){
			rsuiteId = args.getFirstValue("rsuiteId");
		}
		

		OxygenIntegrationAdvisorFactory advisorFactory = new OxygenIntegrationAdvisorFactory();
		FindOxygenAdvisorResult advisorResult = advisorFactory.findAndCreateOxygenIntegrationAdvisor(context);
		
		advisor = advisorResult.getOxygenIntegrationAdvisor();

		String hostAddress = WebServiceUtils.getHostFromWsArguments(args);
		oxygenWebEditingContext = new OxygenWebEditingContext(
				context.getSession(), hostAddress, advisorResult.getCustomPluginId());
		
		this.context = context;
	}

	public MoToLaunch getMoToLaunch() throws RSuiteException {

		User user = context.getSession().getUser();
		
		ManagedObjectService moService = context.getManagedObjectService();
		ManagedObject originalMo = moService.getManagedObject(user, rsuiteId);
		ManagedObject targetMo = originalMo;

		if (originalMo == null) {
			throw new RSuiteException("Unable to find mo with id" + rsuiteId);
		}
		
		if (!originalMo.isOriginalMO()){
			targetMo = moService.getManagedObject(user, originalMo.getTargetId());
		}

		return getMoToLaunch(context, user, originalMo, targetMo);
	}

	public OxygenWebEditingContext getOxygenWebEditingContext() {
		return oxygenWebEditingContext;
	}
	
	

	public IOxygenIntegrationAdvisor initializeAndGetAdvisor(ManagedObject mo) {
		advisor.initialize(oxygenWebEditingContext);
		return advisor;
	}

	
	private static MoToLaunch getMoToLaunch(ExecutionContext context,
			User user, ManagedObject originalMo, ManagedObject targetMo)
			throws RSuiteException {

		if (targetMo.isNonXml()) {
			throw new RSuiteException("MO is not an XML document");
		}
		
		ManagedObjectService moSvc = context.getManagedObjectService();
		String xpath = "";

		if (!MoUtils.isTopMo(context, targetMo)) {
			OpenSubMoPolicy subMoPolicy = OxygenConfigurationUtils
					.readOpenSubMoConfiguration(context);

			if (subMoPolicy.getOpenSubMoPolicy() == SubMoPolicy.PARENT_TOPIC_MO) {

				Set<String> topicSubMo = subMoPolicy.getTopicSubMos();

				ManagedObject parentMo = targetMo;
				// moSvc.getParentManagedObject(user, mo);

				xpath = "//*[@*[local-name() = 'rsuiteId'] = '" + targetMo.getId()
						+ "']";

				while (parentMo != null) {

					String name = parentMo.getElement().getNodeName();

					if (topicSubMo != null && topicSubMo.contains(name)) {
						targetMo = parentMo;
						break;
					}

					targetMo = parentMo;
					parentMo = moSvc.getParentManagedObject(user, targetMo);
				}

			} else if (subMoPolicy.getOpenSubMoPolicy() == SubMoPolicy.TOP_MO) {
				xpath = "//*[@*[local-name() = 'rsuiteId'] = '" + targetMo.getId()
						+ "']";
				targetMo = MoUtils.getTopManagedObject(context, targetMo.getId());
			}


		}
		
		String moRefId = null;
		if (originalMo.getTargetId() != null) {						
			moRefId = originalMo.getId();
		}
		
		if (targetMo.isCheckedoutButNotByUser(user)){
			throw new RSuiteException("Unable to launch the editor. Becaue the MO is alrady checked out by another user");
		}
		
		if (targetMo.isSubMOCheckedOutNotByUser(user.getUserId())){
			throw new RSuiteException("Unable to launch the editor. Becaue one of the sub MOs is already checked out by another user");
		}
		
		//TODO check is should be move outside this method
		if (!targetMo.isCheckedout()) {
			moSvc.checkOut(user, targetMo.getId());
		}

		return new MoToLaunch(targetMo, xpath, moRefId);
	}
}
