package com.arcadsoftware.aev.core.osgi;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.log.LogService;

@Component(service = GlobalLogService.class)
public class GlobalLogService {
	private LogService logService;

	public void debug(final String message) {
		logService.log(LogService.LOG_DEBUG, message);
	}

	public void debug(final String message, final Throwable e) {
		logService.log(LogService.LOG_DEBUG, message, e);
	}

	public void debug(final Throwable e) {
		logService.log(LogService.LOG_DEBUG, e.getLocalizedMessage(), e);
	}

	public void error(final String message, final Throwable e) {
		logService.log(LogService.LOG_ERROR, message, e);
	}

	public void log(final String message) {
		logService.log(LogService.LOG_INFO, message);
	}

	public void log(final String message, final Throwable e) {
		logService.log(LogService.LOG_INFO, message, e);
	}

	public void log(final Throwable e) {
		logService.log(LogService.LOG_INFO, e.getLocalizedMessage(), e);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private void setLogService(final LogService logService) {
		this.logService = logService;
	}

	public void warn(final String message) {
		logService.log(LogService.LOG_WARNING, message);
	}

	public void warn(final String message, final Throwable e) {
		logService.log(LogService.LOG_WARNING, message, e);
	}
}
