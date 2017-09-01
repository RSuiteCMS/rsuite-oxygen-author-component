package com.rsicms.rsuite.editors.oxygen.applet.domain.logging;

import java.io.File;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.rsicms.rsuite.editors.oxygen.applet.extension.temp.OxygenTempFolderManager;

public class LogConfigurator {

	public void configureFileLogging(
			OxygenTempFolderManager tempFolderManager) {

		File logFile = getLogFile(tempFolderManager);
		Appender fileAppender = createFileLogAppender(logFile);
		Logger.getRootLogger().addAppender(fileAppender);
		System.setOut(new LoggingPrintStream(System.out));
		System.setErr(new LoggingPrintStream(System.err));
	}
	

	private Appender createFileLogAppender(File logFile) {
		DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();
		fileAppender.setName("FileLogger");
		fileAppender.setFile(logFile.getAbsolutePath());
		fileAppender.setLayout(new PatternLayout("%d %-5p %m%n"));
		fileAppender.setThreshold(Level.INFO);
		fileAppender.setAppend(true);
		fileAppender.activateOptions();
		return fileAppender;
	}

	private static File getLogFile(OxygenTempFolderManager tempFolderManager) {
		File logFolder = new File(tempFolderManager.getTempFolder(), "log");
		logFolder.mkdirs();
		File logFile = new File(logFolder, "oxygen.log");
		return logFile;
	}
	
	
	public void configureConsoleLogging() {
		Logger.getRootLogger().addAppender(createConsleLogAppender());	
	}

	public Appender createConsleLogAppender() {
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout("%-5p %m%n"));
		consoleAppender.setThreshold(Level.WARN);		
		return consoleAppender;
	}
	
}
