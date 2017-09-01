package com.rsicms.rsuite.editors.oxygen.applet.standalone;

import static com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneArgumentsParser.parseStartupArgument;
import static com.rsicms.rsuite.editors.oxygen.applet.standalone.OxygenStandaloneArgumentsParser.parseStartupParameters;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang.StringUtils;
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
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					try {
						createAndShowApplication(args);
					} catch (OxygenIntegrationException e) {
						logger.error(e, e);
					}
				}
			});
			
		} catch (Exception e) {
			logger.error(e, e);
		}

	}

	public static JFrame createAndShowApplication(final String[] args)
			throws OxygenIntegrationException {

		final OxygenAppletStartupParmaters startupParameters = getStartUpParameters(args);
		configureLog(startupParameters);
		
		final OxygenStandaloneFrame frame = new OxygenStandaloneFrame();
		frame.setSize(650, 250);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		

		final JPanel progressPanel = createLoadingScreen();
		frame.add(progressPanel, BorderLayout.NORTH);

		Thread thread = new Thread() {
			public void run() {
				try {
					createAndShowOxygenComponent(frame, startupParameters, progressPanel);					
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					frame.setSize(dim.width, dim.height);
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
			OxygenStandaloneFrame frame, OxygenAppletStartupParmaters startupParameters, JPanel progressPanel)
			throws OxygenIntegrationException {

		OxygenOpenDocumentParmaters documentParameters = new OxygenOpenDocumentParmaters();
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

		String moId = startupParameters.getParameterValue(OxygenAppletStartupParmatersNames.MO_ID_TO_OPEN);
		if (StringUtils.isNotEmpty(moId)) {
			openDocumentManager.openDocumentInANewTab(moId);

		}

		frame.add(component);

		frame.remove(progressPanel);
		return component.getParentFrame();

	}

	private static OxygenAppletStartupParmaters getStartUpParameters(
			String[] args) throws OxygenIntegrationException {
		OxygenAppletStartupParmaters startupParameters = null;
		

		if (args.length == 1) {
			Map<String, String> startupArguments = parseStartupArgument(args[0]);
			startupParameters = parseStartupParameters(startupArguments);			
		} else {
			startupParameters = parseStartupParameters(args);
		}
		return startupParameters;
	}

	private static JPanel createLoadingScreen() throws OxygenIntegrationException {
		return new OxygenLoadingPanel();
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
