package com.rsicms.rsuite.editors.oxygen.integration.domain;

import java.util.Comparator;

import com.reallysi.rsuite.api.ManagedObject;

public class ManagedObjectNameComparator implements Comparator<ManagedObject> {

	@Override
	public int compare(ManagedObject o1, ManagedObject o2) {
		if (o1 == null && o2 == null){
			return 0;
		}
		
		if (o1 == null && o2 != null){
			return -1;
		}
		
		if (o1 != null && o2 == null){
			return 1;
		}
		
		try{
			String firstObjectDisplayName = o1.getDisplayName();
			String secondObjectDisplayName = o2.getDisplayName();
			
			if (firstObjectDisplayName == secondObjectDisplayName){
				return 0;
			}
			
			if (firstObjectDisplayName != null){
				return firstObjectDisplayName.compareToIgnoreCase(secondObjectDisplayName);
			}else{
				return -1;
			}
			
		}catch (Exception e){
			//
		}
		
		
		return 0;
	}


}
