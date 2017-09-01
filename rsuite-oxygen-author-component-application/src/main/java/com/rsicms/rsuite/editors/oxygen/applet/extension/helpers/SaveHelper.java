package com.rsicms.rsuite.editors.oxygen.applet.extension.helpers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.JDialog;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsActions;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICommand;
import com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.save.RSuiteSaveCommand;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenUtils;
import com.rsicms.rsuite.editors.oxygen.applet.components.IOxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenDocument;
import com.rsicms.rsuite.editors.oxygen.applet.components.OxygenMainComponent;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.DialogHelper;
import com.rsicms.rsuite.editors.oxygen.applet.components.dialogs.ProgressDialog;
import com.rsicms.rsuite.editors.oxygen.applet.extension.actions.SaveProgressListener;

public class SaveHelper {

	private OxygenMainComponent component;
	
	private ICmsActions cmsAction;
	
	private IOxygenDocument documentComponent;

	public SaveHelper(OxygenMainComponent component, IOxygenDocument documentComponent) {
		this.component = component;
		this.cmsAction = component.getCmsCustomization().getCmsActions();
		this.documentComponent = documentComponent;
	}

	public void save() {
		ProgressDialog dialog = createProgressDialog();

		List<ICommand> commandList = new ArrayList<ICommand>();
		commandList.add(getSaveCommand(dialog));

		executeBackgroundAction(dialog, commandList);
	}

	private ProgressDialog createProgressDialog() {
		DialogHelper dialogHelper = component.getDialogHelper();
		ProgressDialog dialog = dialogHelper.createProgressDialog("Saving...",
				50, 250);
		return dialog;
	}

	private void executeBackgroundAction(ProgressDialog dialog,
			List<ICommand> commandList) {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		SaveTask swingWorker = new SaveTask(commandList, latch);
		swingWorker.addPropertyChangeListener(new SwingWorkerCompletionWaiter(
				dialog, swingWorker));
		swingWorker.execute();
		
		try {
			latch.await();			
		} catch (InterruptedException e) {
			OxygenUtils.handleException(logger, e);	
		}
	}

	public void saveCheckInClose(String versionType, String versionNote) {

		ProgressDialog dialog = createProgressDialog();

		List<ICommand> commandList = new ArrayList<ICommand>();
		commandList.add(getSaveCommand(dialog));
		commandList.add(getCheckInAndCloseCommand(versionType, versionNote));

		executeBackgroundAction(dialog, commandList);
	}

	private ICommand getSaveCommand(final ProgressDialog dialog) {
		return new RSuiteSaveCommand(new SaveProgressListener(dialog.getProgressBar()), cmsAction, documentComponent);
	}

	private ICommand getCheckInAndCloseCommand(final String versionType,
			final String versionNote) {
		
		final OxygenDocument documentComponent = component.getActiveDocumentComponent();
		
		return new ICommand() {

			@Override
			public void execute() throws Exception {
				cmsAction.checkInDocument(documentComponent.getDocumentUri(), versionType, versionNote);
			}

			@Override
			public void executeAfter() {
				component.closeDocument(documentComponent);				
			}
		};
	}

	private Logger logger = Logger.getLogger(this.getClass());

	class SwingWorkerCompletionWaiter implements PropertyChangeListener {
		private JDialog dialog;

		private SaveTask task;

		public SwingWorkerCompletionWaiter(JDialog dialog, SaveTask task) {
			this.dialog = dialog;
			this.task = task;
		}

		public void propertyChange(PropertyChangeEvent event) {
			if ("state".equals(event.getPropertyName())
					&& SwingWorker.StateValue.DONE == event.getNewValue()) {
				dialog.setVisible(false);
				dialog.dispose();

				String error = task.getError();
				if (error != null) {
					DialogHelper dialogHelper = component.getDialogHelper();
					dialogHelper.showErrorMessage(error);
				} else {
					List<ICommand> commandList = task.getCommandList();
					for (ICommand command : commandList){
						command.executeAfter();
					}					
				}
			}
		}
	}

	class SaveTask extends SwingWorker<Void, Void> {

		private String error;

		private List<ICommand> commandList;
		
		private CountDownLatch latch;

		public SaveTask(List<ICommand> commandList, CountDownLatch latch) {
			this.commandList = commandList;
			this.latch = latch;
		}

		@Override
		public Void doInBackground() {
			try {

				for (ICommand command : commandList) {
					
					command.execute();
				}

			} catch (Exception e1) {
				error = "Unable to save document: " + e1.getMessage();
				OxygenUtils.handleException(logger, e1);
			}finally{
				if (latch != null){
					latch.countDown();
				}
			}
			return null;
		}

		public String getError() {
			return error;
		}
		
		public List<ICommand> getCommandList(){
			return commandList;
		}
	}

}
