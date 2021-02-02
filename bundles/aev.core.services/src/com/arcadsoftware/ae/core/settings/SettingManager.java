package com.arcadsoftware.ae.core.settings;

import java.lang.reflect.Method;

import com.arcadsoftware.ae.core.utils.XMLUtils;

public class SettingManager {

	public static Setting loadSettings(final String xmlFilename) {
		final SettingParser parser = new SettingParser();
		try {
			XMLUtils.loadXmlDocument(xmlFilename, parser);
			return parser.getSetting();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean setBeanBooleanValue(final Object bean, String setterName, final boolean value) {
		final Class<? extends Object> beanClass = bean.getClass();
		try {
			setterName = setterName.substring(0, 1).toUpperCase() + setterName.substring(1);
			final Method method = beanClass.getMethod("set" + setterName, Boolean.class); //$NON-NLS-1$
			method.invoke(bean, value);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

	public static boolean setBeanStringValue(final Object bean, String setterName, final String value) {
		final Class<? extends Object> beanClass = bean.getClass();
		try {
			setterName = setterName.substring(0, 1).toUpperCase() + setterName.substring(1);
			final Method method = beanClass.getMethod("set" + setterName, String.class); //$NON-NLS-1$
			method.invoke(bean, value);
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

}
