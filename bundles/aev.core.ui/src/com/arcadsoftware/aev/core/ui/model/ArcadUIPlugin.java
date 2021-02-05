package com.arcadsoftware.aev.core.ui.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.model.IArcadPlugin;
import com.arcadsoftware.aev.core.osgi.GlobalLogService;
import com.arcadsoftware.aev.core.osgi.ServiceRegistry;

public abstract class ArcadUIPlugin extends AbstractUIPlugin implements IArcadPlugin, IHelperImage {
	private class DecoratorImageDescriptor extends CompositeImageDescriptor {
		private final ImageData baseImage;
		private final ImageData overlay;

		/**
		 * Constructor for ArcadCompositeImageDescriptor.
		 */
		public DecoratorImageDescriptor(final ImageData baseImage, final ImageData overlay) {
			super();
			this.baseImage = baseImage;
			this.overlay = overlay;
		}

		@Override
		protected void drawCompositeImage(final int width, final int height) {
			drawImage(baseImage, 0, 0);
			drawImage(overlay, 0, 8);
		}

		@Override
		protected Point getSize() {
			return new Point(16, 16);
		}
	}

	protected static final String ICON_PATH = "icons/";
	protected static final String ICON_PLATFORM_PATH = "platform:/plugin/com.arcadsoftware.documentation.icons/icons/";
	
	public static String getIconPath() {
		return ICON_PATH;
	}

	protected ColorRegistry colorRegistry;
	protected FontRegistry fontRegistry;
	protected Map<String, ImageDescriptor> imageDescriptorRegistry;
	protected ImageRegistry imageRegistry;

	protected IShellProvider shellProvider;

	public ArcadUIPlugin() {
		imageDescriptorRegistry = new HashMap<>();
		imageRegistry = null;
		shellProvider = ArcadUIPlugin.this::getPluginShell;
	}

	public Color getColor(final String key) {
		if (key == null || key.length() == 0) {
			return null;
		}
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			initializeColorRegistry();
		}
		return colorRegistry.get(key);
	}

	public Image getCompositeImage(final Image image, final String decoKey) {
		final ImageDescriptor imgd = getCompositeImageDescriptor(image, decoKey);
		if (imgd != null) {
			return imgd.createImage();
		}
		return null;
	}

	@Override
	public Image getCompositeImage(final String key, final String decoKey) {
		final String id = key.concat("_").concat(decoKey); //$NON-NLS-1$
		getCompositeImageDescriptor(key, decoKey);
		return getImage(id);
	}

	public ImageDescriptor getCompositeImageDescriptor(final Image base, final String decoKey) {
		final ImageDescriptor overlay = getImageDescriptor(decoKey);
		return new DecoratorImageDescriptor(base.getImageData(), overlay.getImageData());
	}

	@Override
	public ImageDescriptor getCompositeImageDescriptor(final String key, final String decoKey) {
		final String id = key.concat("_").concat(decoKey); //$NON-NLS-1$
		DecoratorImageDescriptor deco = (DecoratorImageDescriptor) imageDescriptorRegistry.get(id);
		if (deco == null) {
			final ImageDescriptor main = getImageDescriptor(key);
			final ImageDescriptor overlay = getImageDescriptor(decoKey);
			if (main != null && overlay != null) {
				deco = new DecoratorImageDescriptor(main.getImageData(), overlay.getImageData());
				imageDescriptorRegistry.put(id, deco);
				final Image imgd = deco.createImage();
				imageRegistry.put(id, imgd);
			}
		}
		return deco;
	}

	public Font getFont(final String key) {
		if (key == null || key.length() == 0) {
			return null;
		}
		if (fontRegistry == null) {
			fontRegistry = new FontRegistry();
			initializeFontRegistry();
		}
		return fontRegistry.get(key);
	}

	@Override
	public Image getImage(final String key) {
		if (key == null || key.length() == 0) {
			return null;
		}
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			initializeImageRegistry();
		}
		Image image = null;
		try {
			image = imageRegistry.get(key);
		} catch (final Exception t) {
			MessageManager.addException(t, MessageManager.LEVEL_PRODUCTION);
		}
		return image;
	}

	@Override
	public ImageDescriptor getImageDescriptor(final String key) {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			initializeImageRegistry();
		}
		return imageDescriptorRegistry.get(key);
	}

	public ImageDescriptor getPluginImage(final String fileName) {
		final URL path = getBundle().getEntry("/");//$NON-NLS-1$
		URL fullPathString = null;
		try {
			fullPathString = new URL(path, fileName);
			return ImageDescriptor.createFromURL(fullPathString);
		} catch (final MalformedURLException e) {
			return null;
		}
	}

	@Override
	public String getPluginPath() throws IOException {
		final IPath p = new Path(FileLocator.resolve(getBundle().getEntry("/")).getPath()); //$NON-NLS-1$
		return p.toOSString();
	}

	public Shell getPluginShell() {
		if (getWorkbench() != null && getWorkbench().getActiveWorkbenchWindow() != null) {
			return getWorkbench().getActiveWorkbenchWindow().getShell();
		}
		return null;
	}

	public ImageDescriptor getRegisteredImageDescriptor(final String key) {
		if (key != null) {
			ImageDescriptor result = imageDescriptorRegistry.get(key);
			if (result == null) {
				result = registerImage(key);
			}
			return result;
		}
		return null;
	}

	public IShellProvider getShellProvider() {
		return shellProvider;
	}

	@Override
	public String getVersion() {
		return getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
	}

	protected void initializeColorRegistry() {
		// Do nothing
	}

	protected void initializeFontRegistry() {
		// Do nothing
	}

	protected abstract void initializeImageRegistry();

	/**
	 * Add an image to the plugin image registry.
	 *
	 * @param id
	 *            The image ID
	 * @param fileName
	 *            the plugin local path to the image file.
	 * @return
	 */
	protected ImageDescriptor putImageInRegistry(final String id, final String fileName) {
		final ImageDescriptor fid = getPluginImage(fileName);
		imageRegistry.put(id, fid);
		imageDescriptorRegistry.put(id, fid);
		return fid;
	}

	private ImageDescriptor registerImage(final String key) {
		// getImageDescriptor initialize the imageRegistry.
		ImageDescriptor result = getImageDescriptor(key);
		if (result != null) {
			return result;
		}
		final int pos = key.indexOf(':'); //$NON-NLS-1$
		if (pos > 0) {
			final String bundleId = key.substring(0, pos);
			final String imageKey = key.substring(pos + 1);
			result = AbstractUIPlugin.imageDescriptorFromPlugin(bundleId, imageKey);
			if (result != null) {
				// ImageRegistry is initialized into getImageDescriptor
				imageDescriptorRegistry.put(key, result);
				imageRegistry.put(key, result);
			}
		} else {
			try {
				final URL url = FileLocator.find(getBundle(), new Path(key), null);
				if (url != null) {
					result = ImageDescriptor.createFromURL(url);
					imageDescriptorRegistry.put(key, result);
					imageRegistry.put(key, result);
				}
			} catch (final Exception e) {
				ServiceRegistry.lookupOrDie(GlobalLogService.class).debug(e);
			}
		}
		return result;
	}
}
