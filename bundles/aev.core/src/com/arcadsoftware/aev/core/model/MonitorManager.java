package com.arcadsoftware.aev.core.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe permettant de gérer une liste de IMonitor (ProgressMonitor, Log4j,
 * Socket, message, etc.)
 * 
 * @author dlelong
 * 
 */
public class MonitorManager {

	@SuppressWarnings("unchecked")
	protected ArrayList monitors;
	protected boolean verbose = false;

	@SuppressWarnings("unchecked")
	public MonitorManager() {
		super();
		monitors = new ArrayList();
	}

	public MonitorManager(boolean verbose) {
		this();
		this.verbose = verbose;
	}

	@SuppressWarnings("unchecked")
	public void addMonitor(IMonitor monitor) {
		if (monitors.indexOf(monitor) == -1)
			monitors.add(monitor);
	}

	public boolean removeMonitor(IMonitor monitor) {
		return monitors.remove(monitor);
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int getMonitorNumber() {
		return monitors.size();
	}

	public void clear() {
		monitors.clear();
	}

	public boolean contains(IMonitor monitor) {
		return monitors.contains(monitor);
	}

	public boolean isEmpty() {
		return monitors.isEmpty();
	}

	public IMonitor[] getMonitors() {
		IMonitor[] mons = new IMonitor[monitors.size()];
		for (int i = 0; i < monitors.size(); i++) {
			mons[i] = (IMonitor) monitors.get(i);
		}
		return mons;
	}

	@SuppressWarnings("unchecked")
	public Iterator iterator() {
		return monitors.iterator();
	}
}
