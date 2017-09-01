package com.rsicms.rsuite.editors.oxygen.integration.utils;

import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_ADVISOR_CLASS;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_ADVISOR_PRIORITY;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID;
import static com.rsicms.rsuite.editors.oxygen.integration.api.common.OxygenProperites.PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_OVERWRITE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.rsicms.rsuite.editors.oxygen.integration.advisor.OxygenWebEditingContext;
import com.rsicms.rsuite.editors.oxygen.integration.api.advisor.IOxygenIntegrationAdvisor;

public class OxygenAdvisorUtils {
//TODO review if needed
//	public static IOxygenIntegrationAdvisor getCustomOxygenAdvisor(
//			ExecutionContext context, OxygenWebEditingContext webContext, ClassLoader parentClassLoader)
//			throws RSuiteException {
//
//		PluginPropertyResult resultProperty = PluginPropertiesUtils
//				.getPluginProperty(context,
//						PROPERTY_OXYGEN_ADVISOR_CLASS.getProperty(),
//						PROPERTY_OXYGEN_ADVISOR_PRIORITY.getProperty());
//		
//		try {
//		
//		if (resultProperty != null) {
//			String pluginId = resultProperty.getPluginId();
//			String className = resultProperty.getPropertyValue();
//	
//			@SuppressWarnings("rawtypes")
//			Class clazz = PluginUtils.loadClassFromPlugin(context, parentClassLoader, pluginId, className);
//			IOxygenIntegrationAdvisor advisor = (IOxygenIntegrationAdvisor) clazz
//					.newInstance();
//			webContext.setCustomPluginId(pluginId);
//			
//			return advisor;
//		}
//		
//		}catch (ClassNotFoundException e) {
//			throw new RSuiteException(
//					RSuiteException.ERROR_INTERNAL_ERROR,
//					"Unable to load custom advisor", e);
//		} catch (InstantiationException e) {
//			throw new RSuiteException(
//					RSuiteException.ERROR_INTERNAL_ERROR,
//					"Unable to load custom advisor", e);
//		} catch (IllegalAccessException e) {
//			throw new RSuiteException(
//					RSuiteException.ERROR_INTERNAL_ERROR,
//					"Unable to load custom advisor", e);
//		}
//		
//		return null;
//	}
	
//	public static void collectJarsFromFrameworksPlugins(ExecutionContext context, OxygenWebEditingContext webContext) throws RSuiteException{
//		List<PluginPropertyResult> resultPropertyList = PluginPropertiesUtils
//				.getPluginPropertyList(context,
//						PROPERTY_OXYGEN_FRAMEWORK.getProperty(),
//						null);
//		
//		PluginPropertyResult overwriteProperty = PluginPropertiesUtils.getPluginProperty(context, PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_OVERWRITE.getProperty());
//		
//		Set<String> frameworksIdsToOverwrite = getFrameworksIdsToOverwrite(overwriteProperty);
//		
//
//		for (PluginPropertyResult property  : resultPropertyList ){
//			
//			String frameworkId = property.getPluginProperty(PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID.getProperty());
//			
//			if (StringUtils.isEmpty(frameworkId)){
//				throw new RSuiteException("Missing " + PROPERTY_OXYGEN_FRAMEWORK_PLUGIN_ID.getProperty() + " in plugin framework " + property.getPluginId());
//			}
//			
//			if (frameworksIdsToOverwrite.contains(frameworkId)){
//				continue;
//			}
//			
//			webContext.addFrameworkfJar(property.getPluginId(), property.getPropertyValue());
//		}
//	}

//	private static Set<String> getFrameworksIdsToOverwrite(
//			PluginPropertyResult overwriteProperty) {
//		Set<String> frameworksIdsToOverwrite = new HashSet<String>();
//		
//		if (overwriteProperty != null){
//			
//			for (String id : overwriteProperty.getPropertyValue().split(",")){
//				id = id.trim();
//				if (StringUtils.isNotEmpty(id)){
//					frameworksIdsToOverwrite.add(id);
//				}
//			}
//		}
//		
//		return frameworksIdsToOverwrite;
//	}
}
