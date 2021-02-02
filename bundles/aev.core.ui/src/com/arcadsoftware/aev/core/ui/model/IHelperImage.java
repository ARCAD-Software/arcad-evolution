package com.arcadsoftware.aev.core.ui.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public interface IHelperImage {
	Image getCompositeImage(String key, String decoKey);

	ImageDescriptor getCompositeImageDescriptor(String key, String decoKey);

	Image getImage(String key);

	ImageDescriptor getImageDescriptor(String key);
}
