package com.arcadsoftware.aev.core.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe permettant de g�rer une liste de IMonitor (ProgressMonitor, Log4j, Socket, message, etc.)
 *
 * @author dlelong
 */
public class MonitorManager {

	protected ArrayList<IMonitor> monitors;
	protected boolean verbose = false;

	public MonitorManager() {
		super();
		monitors = new ArrayList<>();
	}

	public MonitorManager(final boolean verbose) {
		this();
		this.verbose = verbose;
	}

	public void addMonitor(final IMonitor monitor) {
		if (monitors.indexOf(monitor) == -1) {
			monitors.add(monitor);
		}
	}

	public void clear() {
		monitors.clear();
	}

	public boolean contains(final IMonitor monitor) {
		return monitors.contains(monitor);
	}

	public int getMonitorNumber() {
		return monitors.size();
	}

	public IMonitor[] getMonitors() {
		final IMonitor[] mons = new IMonitor[monitors.size()];
		for (int i = 0; i < monitors.size(); i++) {
			mons[i] = monitors.get(i);
		}
		return mons;
	}

	public boolean isEmpty() {
		return monitors.isEmpty();
	}

	public boolean isVerbose() {
		return verbose;
	}

	public Iterator<IMonitor> iterator() {
		return monitors.iterator();
	}

	public boolean removeMonitor(final IMonitor monitor) {
		return monitors.remove(monitor);
	}

	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}
}
