package com.arcadsoftware.aev.core.model;

/**
 * Interface à implémenter pour toute classe voulant être gérée par le MonitorManager
 * Elle reprend les différentes étapes d'une ArcadAction et permet d'ajouter un message
 * à chaque étape et d'informer de l'avancement avec le nombre d'étape franchie
 * @author dlelong
 *
 */
public interface IMonitor {

	public void begin(String message, int totalNumberOfStep);
	public void initialize(String message, int numberOfStep);
	public void beforeExecute(String message, int numberOfStep);
	public void progress(String message, int numberOfStep);
	public void afterExecute(String message, int numberOfStep);
	public void end(String message);
}
