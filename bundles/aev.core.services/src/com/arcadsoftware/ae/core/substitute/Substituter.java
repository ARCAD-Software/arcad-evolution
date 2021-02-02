package com.arcadsoftware.ae.core.substitute;

import com.arcadsoftware.ae.core.utils.Utils;

public class Substituter {
	private static Substituter instance = new Substituter();

	public static Substituter getInstance() {
		return instance;
	}

	private Substituter() {
		final String arcadHome = Utils.getHomeDirectory();
		System.setProperty(IAEAConstants.AEA_ARCAD_HOME, arcadHome);
	}

	public String substitute(String value) {
		final String[] variables = Utils.toSubstitute(value);
		for (final String variable : variables) {
			value = substituteProperty(value, variable);
		}
		return value;
	}

	public String substituteProperty(String value, final String propertyName) {
		value = Utils.substituteProperty(value, propertyName);
		return value;
	}

}
