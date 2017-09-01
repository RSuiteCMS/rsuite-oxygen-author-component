package com.rsicms.rsuite.editors.oxygen.applet.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApplicationTimer {

	private static final ScheduledExecutorService worker = 
			  Executors.newSingleThreadScheduledExecutor();

	public void schedule(Runnable task, long delay){
		
		worker.schedule(task, delay, TimeUnit.MILLISECONDS);
	}

}
