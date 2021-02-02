package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.arcadsoftware.aev.core.tools.CoreLabels;
import com.arcadsoftware.aev.core.ui.model.IHelperImage;

public class CoreUILabels extends CoreLabels {

	private static CoreUILabels instance;

	public static Image getCompositeImage(final String key, final String decoKey) {
		return getInstance().getImageHelper().getCompositeImage(key, decoKey);
	}

	public static ImageDescriptor getCompositeImageDescriptor(final String key, final String decoKey) {
		return getInstance().getImageHelper().getCompositeImageDescriptor(key, decoKey);
	}

	public static Image getImage(final String key) {
		return getInstance().getImageHelper().getImage(key);
	}

	public static ImageDescriptor getImageDescriptor(final String key) {
		return getInstance().getImageHelper().getImageDescriptor(key);
	}

	public static CoreUILabels getInstance() {
		if (instance == null) {
			instance = new CoreUILabels();
		}
		return instance;
	}

	public static String resString(final String res) {
		return getInstance().getHelper().resString(res);
	}

	private IHelperImage helperImage;

	public CoreUILabels() {
		super();
	}

	public IHelperImage getImageHelper() {
		return helperImage;
	}

	public void setImageHelper(final IHelperImage helperImage) {
		this.helperImage = helperImage;
	}

}
