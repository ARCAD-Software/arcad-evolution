package com.arcadsoftware.ae.core.logger.router;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import com.arcadsoftware.ae.core.exceptions.ArcadRuntimeException;
import com.arcadsoftware.ae.core.logger.formatter.AbstractMessageFormatter;
import com.arcadsoftware.ae.core.logger.formatter.impl.BasicMessageFormatter;
import com.arcadsoftware.ae.core.logger.messages.AbstractMessage;
import com.arcadsoftware.ae.core.logger.messages.Messages;
import com.arcadsoftware.ae.core.logger.messages.impl.ErrorMessage;
import com.arcadsoftware.ae.core.utils.Utils;

public abstract class AbstractMessageRouter {
	protected class DualStream extends PrintStream {
		protected boolean canLog;
		protected File errorLogFile;
		protected boolean isErrorStream;
		protected File standardLogFile;
		private final SimpleDateFormat timeStampFormat = new SimpleDateFormat("[yyyy-MM-dd][HH:mm:ss] ");

		public DualStream(final OutputStream stream1, final boolean isErrorStream) {
			super(stream1);
			this.isErrorStream = isErrorStream;
			try {
				final Path directory = Paths.get(Utils.getHomeDirectory() + "/logs/");
				if (Files.notExists(directory)) {
					Files.createDirectories(directory);
				}
				standardLogFile = new File(directory + "service_providers_stdout.log")
						.getCanonicalFile();
				errorLogFile = new File(directory + "service_providers_stderr.log")
						.getCanonicalFile();
			} catch (final IOException e) {
				interceptMessage(new ErrorMessage("DualStream",
						"Could not initialize DualStream output logs:" + e.getLocalizedMessage()));
			}
			canLog = standardLogFile != null && errorLogFile != null;
		}

		protected File getLogFile(final File logFile) {
			boolean createdOrExisted = false;
			try {
				if (logFile.exists()) {
					if (logFile.length() > 2097152) {
						renameLocalLogFile(logFile, 1);
						createdOrExisted = logFile.createNewFile();
					} else {
						createdOrExisted = true;
					}
				} else {
					createdOrExisted = logFile.createNewFile();
				}
			} catch (final IOException e) {
				return null;
			}

			return createdOrExisted ? logFile : null;
		}

		private String getTimeStamp() {
			return timeStampFormat.format(new Date());
		}

		protected boolean renameLocalLogFile(final File localLogFile, final int number) {
			final String path = localLogFile.getParent();
			String name = localLogFile.getName();
			String extension = "";
			final int lastPoint = name.lastIndexOf('.');
			if (lastPoint > 0) {
				extension = name.substring(lastPoint);
				name = name.substring(0, lastPoint) + "_";
			}
			final File backLocalLogFile = new File(
					path + File.separator + name + String.format("%1$03d", number) + extension);
			if (backLocalLogFile.exists()) {
				renameLocalLogFile(localLogFile, number + 1);
			}
			return localLogFile.renameTo(backLocalLogFile);
		}

		@Override
		public void write(final byte[] buf, final int off, final int len) {
			super.write(buf, off, len);
			final String message = new String(buf, off, len).trim();
			if (message.length() > 0) {
				if (isErrorStream) {
					writeLine(getLogFile(errorLogFile), message);
				} else {
					writeLine(getLogFile(standardLogFile), message);
				}
			}
		}

		protected void writeLine(final File logFile, String message) {
			message = getTimeStamp() + message + "\n";
			try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
				fos.write(message.getBytes(StandardCharsets.UTF_8));
				fos.flush();
			} catch (final Exception e) {
				throw new ArcadRuntimeException("Could not write into " + logFile, e);
			}
		}
	}

	protected String data = null;

	protected AbstractMessageFormatter formatter = null;

	// <FM number="2016/00133" version="10.06.00" date="1 Mar 2016" user="SJU">
	protected final ReentrantLock interceptionLock = new ReentrantLock();
	protected Messages messages = new Messages();

	// </FM>
	protected boolean standardOutputCaught;

	public AbstractMessageRouter() {
		catchStandardOutput();
	}

	/**
	 * Override to NOT intercept messages directed to the standard/error output.
	 */
	protected boolean canCatchStandardOutput() {
		return true;
	}

	protected void catchStandardOutput() {
		if (canCatchStandardOutput() && !standardOutputCaught) {
			System.setOut(new DualStream(System.out, false));
			System.setErr(new DualStream(System.err, true));
			standardOutputCaught = true;
		}
	}

	protected abstract void doFinalize();

	protected abstract void doInitialize();

	protected abstract void doIntercept();

	public void finalizeMessages() {
		doFinalize();
	}

	public String getData() {
		return getData(false);
	}

	public String getData(final boolean refresh) {
		if (data == null || refresh) {
			if (formatter == null) {
				initMessageFormatter();
			}
			data = formatter.format(messages);
		}
		return data;
	}

	/**
	 * @return Renvoie formatter.
	 */
	public AbstractMessageFormatter getFormatter() {
		return formatter;
	}

	public void initializeMessages() {
		messages.clear();
		data = null;
		doInitialize();
	}

	protected void initMessageFormatter() {
		formatter = new BasicMessageFormatter();
	}

	public void interceptMessage(final AbstractMessage message) {
		interceptionLock.lock();

		messages.add(message);
		doIntercept();

		interceptionLock.unlock();
	}

	public void setFormatter(final AbstractMessageFormatter formatter) {
		this.formatter = formatter;
	}
}
