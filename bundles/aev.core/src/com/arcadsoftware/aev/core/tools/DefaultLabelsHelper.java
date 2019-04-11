/*
 * Created on Sep 21, 2006
 */
package com.arcadsoftware.aev.core.tools;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;


/**
 * @author MD
 */
public class DefaultLabelsHelper implements IHelperRessource {

    public DefaultLabelsHelper() {
        super();
    }

    @Override
	public String resString(String res) {
        return res;
    }

    @Override
	public ImageDescriptor getImageDescriptor(String key) {
		return null;
	}

    @Override
	public Image getImage(String key) {
		return null;
	}

    @Override
	public Image getCompositeImage(String key, String decoKey) {
		return null;
	}

    @Override
	public ImageDescriptor getCompositeImageDescriptor(String key, String decoKey) {
		return null;
	}

}
