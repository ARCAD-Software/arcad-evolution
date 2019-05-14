package com.arcadsoftware.aev.core.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public interface IHelperImage {
	public ImageDescriptor getImageDescriptor(String key);
	public Image getImage(String key);
	public Image getCompositeImage(String key,String decoKey);
	public ImageDescriptor getCompositeImageDescriptor(String key,String decoKey);
}
