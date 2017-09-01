package com.rsicms.rsuite.editors.oxygen.integration.api.advisor;

import com.reallysi.rsuite.api.Session;

/**
 * Additional context provided to an editing integration advisor such as
 */

public interface IOxygenWebEditingContext {

	/**
	 * @return The user RSuite session for the editing activity.
	 */
	public Session getSession();
	
	public String getHostAddress();
	
	public String getCustomPluginId();
	
}
