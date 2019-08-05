package com.arcadsoftware.ae.core.settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dom4j.DocumentException;

import com.arcadsoftware.ae.core.utils.XMLUtils;

public class SettingManager {


	
	public static Setting loadSettings(String xmlFilename) {
		SettingParser parser = new SettingParser();
		try {
			XMLUtils.loadXmlDocument(xmlFilename,  parser);
			return parser.getSetting();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean setBeanStringValue(Object bean,String setterName, String value) {
		Class<? extends Object> beanClass = bean.getClass();
		try {
			setterName = setterName.substring(0,1).toUpperCase()+setterName.substring(1);
			Method method = beanClass.getMethod("set"+setterName, String.class); //$NON-NLS-1$
			method.invoke(bean, value);
			return true;
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
	}	
	
	public static boolean setBeanBooleanValue(Object bean,String setterName, boolean value) {
		Class<? extends Object> beanClass = bean.getClass();
		try {
			setterName = setterName.substring(0,1).toUpperCase()+setterName.substring(1);
			Method method = beanClass.getMethod("set"+setterName, Boolean.class); //$NON-NLS-1$
			method.invoke(bean, value);
			return true;
		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
	}		
	
}
