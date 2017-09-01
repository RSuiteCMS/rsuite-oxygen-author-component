package com.rsicms.rsuite.editors.oxygen.launcher.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainLogger {

	private static Logger logger = Logger.getLogger("oxygen.launcher.logger");
	
	static{
		setUpLogger();
	}

	private static void setUpLogger(){
		Handler fh = new ConsoleHandler();
	    fh.setLevel (Level.FINE);

	    
	    logger.addHandler (fh);


	    logger.setLevel (Level.FINE);
	}


	public static Logger getLauncherLogger(){
		return logger;
	}
	
}
