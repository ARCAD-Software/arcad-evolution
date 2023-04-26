package com.arcadsoftware.aev.icons;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public enum Brand {
	ARCAD_LOGO_128("brands/arcad-logo-128.png"),
	ARCAD_LOGO_16("brands/arcad-logo-16.png"),
	ARCAD_LOGO_256("brands/arcad-logo-256.png"),
	ARCAD_LOGO_32("brands/arcad-logo-32.png"),
	ARCAD_LOGO_48("brands/arcad-logo-48.png"),
	ARCAD_LOGO_64("brands/arcad-logo-64.png"),
	ARCAD_LOGO_BLANK_BORDER_TEXT_144("brands/arcad-logo-blank-border-text-144.png"),
	ARCAD("brands/arcad.png");
	
	private Image image;
	private ImageDescriptor imageDescriptor;
	private final String path;

	Brand(final String path) {
		this.path = path;
	}

	public Image image() {
		if (image == null) {
			load();
		}

		return image;
	}

	public ImageDescriptor imageDescriptor() {
		if (imageDescriptor == null) {
			load();
		}

		return imageDescriptor;
	}

	private void load() {
		try {
			final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			imageDescriptor = ImageDescriptor.createFromURL(bundle.getEntry(path));
		} catch (final Throwable e) {
			e.printStackTrace();
			imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
		}

		image = imageDescriptor.createImage(true, Display.getDefault());
	}

	public String path() {
		return path;
	}
}
