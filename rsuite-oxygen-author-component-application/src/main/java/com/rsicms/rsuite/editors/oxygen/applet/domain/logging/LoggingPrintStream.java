package com.rsicms.rsuite.editors.oxygen.applet.domain.logging;

import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;

public class LoggingPrintStream extends PrintStream {

	private Logger logger = Logger.getLogger(getClass());

	public LoggingPrintStream(OutputStream out) {
		super(out);
	}
	
	@Override
	public void println(Object object) {
		logger.info(object);
	}

	@Override
	public void println(String line) {
		logger.info(line);
	}
	
	
	
}
