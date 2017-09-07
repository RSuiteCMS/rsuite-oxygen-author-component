package com.rsicms.rsuite.editors.oxygen.applet.standalone;

import static com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneArgumentsParser.parseStartupArgument;
import static com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneArgumentsParser.parseStartupParameters;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.OxygenIntegrationException;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.domain.communication.internal.server.SocketServerRunner;
import com.rsicms.rsuite.editors.oxygen.applet.domain.logging.LogConfigurator;
import com.rsicms.rsuite.editors.oxygen.applet.extension.launch.OpenDocumentManager;
import com.rsicms.rsuite.editors.oxygen.applet.extension.temp.OxygenTempFolderManager;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmaters;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenAppletStartupParmatersNames;
import com.rsicms.rsuite.editors.oxygen.applet.parameters.OxygenOpenDocumentParmaters;

public class OxygenStandaloneRunner {

	private static Logger logger = Logger
			.getLogger(OxygenStandaloneRunner.class);

	public static void main(final String[] args) throws Exception {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			createAndShowApplication(args);
		} catch (Exception e) {
			logger.error(e, e);
		}

	}

	private static JLabel createInitializeLabel() {
		JLabel label = new JLabel("Initializing Oxygen Application...");
		label.setName("initialize");
		return label;
	}

	public static JFrame createAndShowApplication(final String[] args)
			throws OxygenIntegrationException {

		final OxygenStandaloneFrame frame = new OxygenStandaloneFrame();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(dim.width, dim.height);

		frame.setVisible(true);

		final JPanel progressPanel = createLoadingScreen();
		frame.add(progressPanel, BorderLayout.NORTH);

		Thread thread = new Thread() {
			public void run() {
				try {
					createAndShowOxygenComponent(frame, args);
					frame.remove(progressPanel);
					SwingUtilities.updateComponentTreeUI(frame);
				} catch (Exception e) {
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					OxygenUtils.handleException(logger, e);
					JOptionPane.showMessageDialog(frame,
							"Unable to initialize the application");
					frame.remove(progressPanel);
					SwingUtilities.updateComponentTreeUI(frame);

				}
			}
		};

		thread.start();

		return frame;
	}

	private static JFrame createAndShowOxygenComponent(
			OxygenStandaloneFrame frame, String[] args)
			throws OxygenIntegrationException {

		Map<String, String> startupArguments = null;
		OxygenAppletStartupParmaters startupParameters = null;
		OxygenOpenDocumentParmaters documentParameters = new OxygenOpenDocumentParmaters();

		if (args.length == 1) {
			startupArguments = parseStartupArgument(args[0]);
			startupParameters = parseStartupParameters(startupArguments);
		} else {
			startupParameters = parseStartupParameters(args);
		}

		configureLog(startupParameters);

		final OxygenMainComponent component = OxygenMainComponent
				.initializeComponent(startupParameters, documentParameters,
						false);
		component.getComponentBuilder().cleanUpDocumentViews();
		frame.configureMainFrame(component);

		OpenDocumentManager openDocumentManager = new OpenDocumentManager(
				frame, component);

		SocketServerRunner socketServer = new SocketServerRunner(
				startupParameters
						.getParameterValue(OxygenAppletStartupParmatersNames.BASE_URI),
				openDocumentManager);
		socketServer.start();

		if (startupArguments != null
				&& startupArguments.get("moIdToOpen") != null) {
			String moId = startupArguments.get("moIdToOpen");
			openDocumentManager.openDocumentInANewTab(moId);

		}

		frame.add(component);

		return component.getParentFrame();

	}

	private static JPanel createLoadingScreen() {

		final JPanel progressPanel = new JPanel(new GridLayout(2, 1));
		progressPanel.add(createInitializeLabel());
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar);

		return progressPanel;
	}

	private static void configureLog(
			OxygenAppletStartupParmaters startupParameters)
			throws OxygenIntegrationException {
		String baseUri = startupParameters
				.getParameterValue(OxygenAppletStartupParmatersNames.BASE_URI);
		OxygenTempFolderManager tempFolderManager = new OxygenTempFolderManager(
				baseUri);
		LogConfigurator logConfigurator = new LogConfigurator();
		logConfigurator.configureFileLogging(tempFolderManager);
	}
}
