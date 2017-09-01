package com.rsicms.rsuite.editors.oxygen.applet.common.references;

public enum LookupMethod {

		SEARCH, BROWSE, DEFAULT;
		
		public String getName(){
			return this.toString().toLowerCase();
		}
		
		private static LookupMethod currentMethod = DEFAULT;
		
		public static void setCurrentMethod(LookupMethod lookupMethod){
			currentMethod = lookupMethod;
		}
		
		public static LookupMethod getCurrentMethod(LookupMethod elementLookup){
		
			if (currentMethod == LookupMethod.DEFAULT){
				return elementLookup;
			}
			
			return currentMethod;
		}

		public static LookupMethod getCurrentMethod() {
			return currentMethod;
		}
		
}
