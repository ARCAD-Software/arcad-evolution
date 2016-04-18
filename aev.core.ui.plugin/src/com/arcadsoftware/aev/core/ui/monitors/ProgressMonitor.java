package com.arcadsoftware.aev.core.ui.monitors;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.arcadsoftware.aev.core.messages.MessageDetail;
import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.IMonitor;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;

public class ProgressMonitor implements IMonitor {

	protected boolean progress = false;
	protected boolean initialize = false;
	protected boolean beforeExecute = false;
	protected boolean afterExecute = false;
	protected boolean end = false;
	protected String message = StringTools.EMPTY;
	protected int stepNumber = 1;
	protected Thread t;

	public ProgressMonitor() {
		super();
	}

	public void begin(String beginMessage, int totalNumberOfStep) {
		this.message = beginMessage;
		this.stepNumber = totalNumberOfStep;
		// EvolutionCoreUIPlugin.getShell().getDisplay().asyncExec(new
		// MyRunnable());
		// t = new Thread(new MyRunnable());
		// t.start();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					new ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell()).run(true, false,
							new RunnableCommand());
				} catch (InvocationTargetException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
				} catch (InterruptedException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
				}
			}
		});
		thread.start();
		try {
			thread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void end(String endMessage) {
		end = true;
		this.message = endMessage;
	}

	public void progress(String progressMessage, int numberOfStep) {
		progress = true;
		this.message = progressMessage;
		this.stepNumber = numberOfStep;
	}

	protected class RunnableCommand implements IRunnableWithProgress {
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			final IProgressMonitor myMonitor = monitor;
			monitor.beginTask(message, stepNumber);
			// Thread t = new Thread(new Runnable(){
			// public void run() {
			while (!end) {
				if (initialize) {
					myMonitor.setTaskName(message);
					myMonitor.worked(stepNumber);
					initialize = false;
				}
				if (beforeExecute) {
					myMonitor.setTaskName(message);
					myMonitor.worked(stepNumber);
					beforeExecute = false;
				}
				if (afterExecute) {
					myMonitor.setTaskName(message);
					myMonitor.worked(stepNumber);
					afterExecute = false;
				}
				if (progress) {
					myMonitor.setTaskName(message);
					myMonitor.worked(stepNumber);
					progress = false;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			myMonitor.done();
		}
		// });
		// t.start();
		// }
	}

	// private class MyRunnable implements Runnable{
	// public void run() {
	// try {
	// new
	// ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell())
	// .run(true, false, new RunnableCommand());
	// } catch (InvocationTargetException e) {
	// MessageManager.addException(e,MessageManager.LEVEL_PRODUCTION)
	// .addDetail(MessageDetail.ERROR,this.getClass().toString());
	// } catch (InterruptedException e) {
	// MessageManager.addException(e,MessageManager.LEVEL_PRODUCTION)
	// .addDetail(MessageDetail.ERROR,this.getClass().toString());
	// }
	// }
	// }

	public void afterExecute(String afterMessage, int numberOfStep) {
		afterExecute = true;
		this.message = afterMessage;
		this.stepNumber = numberOfStep;
	}

	public void beforeExecute(String beforeMessage, int numberOfStep) {
		beforeExecute = true;
		this.message = beforeMessage;
		this.stepNumber = numberOfStep;
	}

	public void initialize(String initMessage, int numberOfStep) {
		initialize = true;
		this.message = initMessage;
		this.stepNumber = numberOfStep;
	}
}
