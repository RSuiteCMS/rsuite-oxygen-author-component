package com.rsicms.rsuite.editors.oxygen.launcher.common.utils.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.rsicms.rsuite.editors.oxygen.launcher.common.OxygenApplicationException;

public final class SystemUtils {

	private SystemUtils() {
	}

	public static CommandExecutionResult executeOScommand(
			List<String> commandList, File baseFolder)
			throws OxygenApplicationException {

		return executeOScommand(commandList, baseFolder, true);
	}

	public static CommandExecutionResult executeOScommand(
			List<String> commandList) throws OxygenApplicationException {

		return executeOScommand(commandList, null, true);
	}

	private static CommandExecutionResult executeOScommand(
			List<String> commandList, File baseFolder,
			boolean redirectErrorStream) throws OxygenApplicationException {
		try {
			ProcessBuilder pb = new ProcessBuilder(commandList);
			if (baseFolder != null) {
				pb.directory(baseFolder);
			}

			pb.redirectErrorStream(redirectErrorStream);

			Process proc = pb.start();

			StringBuffer resultOutput = new StringBuffer();

			StreamGobbler errorGobbler = new StreamGobbler(
					proc.getErrorStream());

			StreamGobbler outputGobbler = new StreamGobbler(
					proc.getInputStream());

			outputGobbler.start();
			errorGobbler.start();

			return new CommandExecutionResult(resultOutput,
					outputGobbler.getOutput(), errorGobbler.getOutput(), 0);

		} catch (Exception t) {
			throw new OxygenApplicationException("Unable to execute command "
					+ commandList, t);
		}
	}

}

class StreamGobbler extends Thread {
	private InputStream is;

	private StringBuilder localResult = new StringBuilder();

	StreamGobbler(InputStream is) {
		this.is = is;
	}

	public void run() {

		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		try {

			String line = null;
			while ((line = br.readLine()) != null) {
				localResult.append(line).append("\n");
			}
		} catch (IOException ioe) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}

	public StringBuilder getOutput() {
		return localResult;
	}

}
