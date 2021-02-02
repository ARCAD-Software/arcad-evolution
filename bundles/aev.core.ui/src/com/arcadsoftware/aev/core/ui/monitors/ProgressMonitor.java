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

	protected class RunnableCommand implements IRunnableWithProgress {
		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			final IProgressMonitor myMonitor = monitor;
			monitor.beginTask(message, stepNumber);
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
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			myMonitor.done();
		}
	}

	protected boolean afterExecute = false;
	protected boolean beforeExecute = false;
	protected boolean end = false;
	protected boolean initialize = false;
	protected String message = StringTools.EMPTY;
	protected boolean progress = false;
	protected int stepNumber = 1;

	protected Thread t;

	public ProgressMonitor() {
		super();
	}

	@Override
	public void afterExecute(final String afterMessage, final int numberOfStep) {
		afterExecute = true;
		message = afterMessage;
		stepNumber = numberOfStep;
	}

	@Override
	public void beforeExecute(final String beforeMessage, final int numberOfStep) {
		beforeExecute = true;
		message = beforeMessage;
		stepNumber = numberOfStep;
	}

	@Override
	public void begin(final String beginMessage, final int totalNumberOfStep) {
		message = beginMessage;
		stepNumber = totalNumberOfStep;
		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new ProgressMonitorDialog(EvolutionCoreUIPlugin.getDefault().getPluginShell()).run(true, false,
							new RunnableCommand());
				} catch (final InvocationTargetException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
				} catch (final InterruptedException e) {
					MessageManager.addException(e, MessageManager.LEVEL_PRODUCTION).addDetail(MessageDetail.ERROR,
							this.getClass().toString());
					Thread.currentThread().interrupt();
				}
			}
		});
		thread.start();
		try {
			thread.join(1000);
		} catch (final InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void end(final String endMessage) {
		end = true;
		message = endMessage;
	}

	@Override
	public void initialize(final String initMessage, final int numberOfStep) {
		initialize = true;
		message = initMessage;
		stepNumber = numberOfStep;
	}

	@Override
	public void progress(final String progressMessage, final int numberOfStep) {
		progress = true;
		message = progressMessage;
		stepNumber = numberOfStep;
	}
}
