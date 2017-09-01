package com.rsicms.rsuite.editors.oxygen.integration.domain.bookmarks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reallysi.rsuite.api.ManagedObject;
import com.reallysi.rsuite.api.RSuiteException;
import com.reallysi.rsuite.api.User;
import com.reallysi.rsuite.api.extensions.ExecutionContext;
import com.reallysi.rsuite.api.system.UserPropertiesCatalog;
import com.reallysi.rsuite.service.ManagedObjectService;
import com.rsicms.rsuite.editors.oxygen.integration.domain.ManagedObjectNameComparator;

public class BookmarkManager {

	private static final String BOOKMARK_SEPARATOR = ";";

	private static final String USER_PROPERTY_OXYGEN_BOOKMARKS = "oxygen.bookmarks";
	
	private UserPropertiesCatalog userPropertiesCatalog;
	
	private ManagedObjectService moService;
	
	public BookmarkManager(ExecutionContext context) throws RSuiteException {
		userPropertiesCatalog = context.getUserService().getUserPropertiesCatalog();
		moService = context.getManagedObjectService();
	}

	public void addBookmark(User user, String moId) throws RSuiteException{
		
		
		List<String> bookmarkIdList = getBookmarkIds(user);
		
		if (!bookmarkIdList.contains(moId)){			
			bookmarkIdList.add(moId);
			persistBookmarks(user, bookmarkIdList);
		}
	}

	private List<String> getBookmarkIds(User user) {
		Map<String, String> userProperties = getUserPropreties(user);
		
		String oxygenBookmarks = userProperties.get(USER_PROPERTY_OXYGEN_BOOKMARKS);
		if (StringUtils.isEmpty(oxygenBookmarks)){
			oxygenBookmarks = "";
		}
		
		String[] bookmarkArray = oxygenBookmarks.split(BOOKMARK_SEPARATOR);
		
		List<String> bookmarkIdList = new ArrayList<String>();
		for (String bookmark : bookmarkArray){
			if (StringUtils.isNotBlank(bookmark)){
				bookmarkIdList.add(bookmark);
			}
		}
		
		
		return bookmarkIdList;
	}

	private void persistBookmarks(User user, List<String> bookmarkIdList) throws RSuiteException {
		String bookmarkIds = StringUtils.join(bookmarkIdList, BOOKMARK_SEPARATOR);
		userPropertiesCatalog.set(getUserId(user), USER_PROPERTY_OXYGEN_BOOKMARKS, bookmarkIds);
		
	}

	private Map<String, String> getUserPropreties(User user) {
		String userId = getUserId(user);
		Map<String, String> userProperties = userPropertiesCatalog.getProperties(userId);
		
		if (userProperties == null){
			return new HashMap<String, String>();
		}
		return userProperties;
	}
	
	public void removeBookmarks(User user, List<String> moIds) throws RSuiteException{
		
		List<String> bookmarkIdList = getBookmarkIds(user);
		
		
		if (!bookmarkIdList.contains(moIds)){
			bookmarkIdList.removeAll(moIds);
			persistBookmarks(user, bookmarkIdList);
		}
	}
	
	public void removeBookmark(User user, String moId) throws RSuiteException{
		List<String> bookmarkIdToRemove = new ArrayList<String>();
		bookmarkIdToRemove.add(moId);
		
		removeBookmarks(user, bookmarkIdToRemove);
	}
	
	public List<ManagedObject> getBookmarksForUser(User user) throws RSuiteException{
		List<String> bookmarkIds = getBookmarkIds(user);
		return convertBookmarkIdsListToMOList(user, bookmarkIds);
	}
	
	private List<ManagedObject> convertBookmarkIdsListToMOList(
			User user, List<String> bookmarkIds) throws RSuiteException {

		List<ManagedObject> bookmarkedMOs = new ArrayList<ManagedObject>();
		
		for (String moId: bookmarkIds){
			ManagedObject managedObject = moService.getManagedObject(user, moId);
			if (managedObject != null){
				bookmarkedMOs.add(managedObject);
			}
		}
	
		sortBookmarksByName(bookmarkedMOs);
		
		return bookmarkedMOs;
	}

	private void sortBookmarksByName(List<ManagedObject> bookmarkedMOs) {
		Collections.sort(bookmarkedMOs, new ManagedObjectNameComparator());
		
	}

	private String getUserId(User user){
		return user.getUserId();
	}
}
