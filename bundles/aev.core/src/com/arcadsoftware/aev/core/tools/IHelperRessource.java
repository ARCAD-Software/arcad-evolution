/*
 * Créé le Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author mhelene
 */
public interface IHelperRessource {
	public String resString(String key);
	public ImageDescriptor getImageDescriptor(String key);
	public Image getImage(String key);
	public Image getCompositeImage(String key,String decoKey);
	public ImageDescriptor getCompositeImageDescriptor(String key,String decoKey);
}
