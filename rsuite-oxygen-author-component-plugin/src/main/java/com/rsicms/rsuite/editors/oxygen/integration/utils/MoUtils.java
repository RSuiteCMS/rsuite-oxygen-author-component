package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ContentAssemblyItem;
import com.reallysi.rsuite.api.ContentAssemblyNodeContainer;
import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.ObjectType;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.VersionSpecifier;
import com.reallysi.rsuite.api.browse.BrowseInfo;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.service.ManagedObjectService;

/**
 * 
 * @deprecated Use class from extension helper when available
 *
 */
@Deprecated
public class MoUtils {

	public static ManagedObject getTopManagedObject(ExecutionContext context, String rsuiteId) throws RSuiteException{
		ManagedObjectService moService = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();

		String parentMoId = moService.getParentManagedObjectId(user, rsuiteId);

		while (parentMoId != null) {
			rsuiteId = parentMoId;
			parentMoId = moService.getParentManagedObjectId(user, parentMoId);
		}

		return moService.getManagedObject(user, rsuiteId);
	}
	
	public static boolean isTopMo(ExecutionContext context, ManagedObject mo) throws RSuiteException{
		
		ManagedObjectService moService = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();
		
		String parentMoId = moService.getParentManagedObjectId(user, mo.getId());
		
		return (parentMoId == null);
	}

	public static ManagedObject getRealMo(ExecutionContext context, String id) throws RSuiteException {
		ManagedObjectService moSvc = context.getManagedObjectService();
		User user = context.getAuthorizationService().getSystemUser();
		
		ManagedObject mo = moSvc.getManagedObject(user, id);
		if (mo == null){
			throw new RSuiteException("Object with id " + id + " not found");
		}
		
		if (StringUtils.isNotEmpty(mo.getTargetId())){
			return moSvc.getManagedObject(user, new VersionSpecifier(mo.getTargetId(), mo.getTargetRevision()));
		}
		
		return mo;
	}

	/**
	 * Extends standard get child managed objects. Add support for smart folders
	 * @param context Execution context
	 * @param user User object 
	 * @param mo managed object
	 * @return returns all children managed objects
	 * @throws RSuiteException
	 */
	public static List<ManagedObject> getManageObjectChildren(
			ExecutionContext context, User user, ManagedObject mo)
			throws RSuiteException {
		ManagedObjectService mosvc = context.getManagedObjectService();
	
	 	BrowseInfo browserInfo = mosvc.getChildManagedObjects(user, mo.getId(), 0, 2000);
	 	List<ManagedObject> childs =  browserInfo.getManagedObjects();
		
		
		if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY) {
			ContentAssemblyNodeContainer caItem = context
					.getContentAssemblyService().getContentAssembly(user,
							mo.getId());
			if (caItem.isDynamic()) {
				childs.addAll(context.getContentAssemblyService()
						.getChildrenObjectsForSearch(user, caItem));
			}
		}
		return childs;
	}

	public static boolean isContentAssembly(ManagedObject mo){
		if (mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY || mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_REF || mo.getObjectType() == ObjectType.CONTENT_ASSEMBLY_NODE){
			return true;
		}
		
		return false;
	}

	public static ContentAssemblyItem getContentAssemblyItemFromMo(ExecutionContext context, ManagedObject mo) throws RSuiteException{
		String caId = mo.getTargetId() == null ? mo.getId() : mo.getTargetId();
		User user = context.getAuthorizationService().getSystemUser();
		ContentAssemblyItem caItem = context.getContentAssemblyService().getContentAssemblyNodeContainer(user, caId);
		return caItem;
	}
}
