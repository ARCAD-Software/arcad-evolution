/*
 * Créé le 28 avr. 04
 *
 * Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */

package com.arcadsoftware.aev.core.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

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

/**
 * @author MD
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et
 *         commentaires
 */
public abstract class ArcadPlugin extends AbstractUIPlugin {
	
	private Hashtable<String,ImageDescriptor> imageDescriptorRegistry;
	private ImageRegistry imageRegistry;
	protected FontRegistry fontRegistry;
	protected ColorRegistry colorRegistry;

	protected static final String ICON_PATH;

	static {
		ICON_PATH = "icons" + File.separatorChar;//$NON-NLS-1$
	}

	private class PluginShellProvider implements IShellProvider {
		ArcadPlugin plugin;

		public PluginShellProvider(ArcadPlugin plugin) {
			this.plugin = plugin;
		}

		public Shell getShell() {
			return plugin.getPluginShell();
		}
	}

	private class DecoratorImageDescriptor extends CompositeImageDescriptor {
		ImageData baseImage;
		ImageData overlay;

		/**
		 * Constructor for ArcadCompositeImageDescriptor.
		 */
		public DecoratorImageDescriptor(ImageData baseImage, ImageData overlay) {
			super();
			this.baseImage = baseImage;
			this.overlay = overlay;
		}

		@Override
		protected void drawCompositeImage(int width, int height) {
			drawImage(baseImage, 0, 0);
			drawImage(overlay, 0, 8);
		}

		@Override
		protected Point getSize() {
			return new Point(16, 16);
		}
	}

	PluginShellProvider shellProvider;

	/**
	 * @param descriptor
	 */
	public ArcadPlugin() {
		super();
		imageDescriptorRegistry = new Hashtable<String,ImageDescriptor>();
		imageRegistry = null;
		shellProvider = new PluginShellProvider(this);
	}

	protected abstract void initializeImageRegistry();

	public static String getIconPath() {
		return ICON_PATH;
	}

	public String getPluginPath() throws IOException {
		IPath p = new Path(FileLocator.resolve(getBundle().getEntry("/")).getPath()); //$NON-NLS-1$
		return p.toOSString();
	}


	protected ImageDescriptor putImageInRegistry(String id, String fileName) {
		ImageDescriptor fid = getPluginImage(fileName);
		imageRegistry.put(id, fid);
		imageDescriptorRegistry.put(id, fid);
		return fid;
	}


	private ImageDescriptor registerImage(String key) {
		int pos = key.indexOf(":");  //$NON-NLS-1$
		ImageDescriptor result = null;
		if (pos>0) {
			String bundleId = key.substring(0,pos);
			String imageKey = key.substring(pos+1);
			result = AbstractUIPlugin.imageDescriptorFromPlugin(bundleId, imageKey);
		} else {
			result = getImageDescriptor(key);					
		}		
		if (result!=null) {
			imageDescriptorRegistry.put(key, result);
			imageRegistry.put(key, result);
		}
		return result;
	}		
	/**
	 * Get an registered image. If this image is not already registered, 
	 * it is first registered.
	 * @param key
	 * @return
	 */
	public ImageDescriptor getRegisteredImageDescriptor(String key) {
		if (key!=null) {			
			ImageDescriptor result = imageDescriptorRegistry.get(key);
			if (result==null) {			
				result = registerImage(key);
			}
			return result;
		}
		return null;
	}	
	
	
	public ImageDescriptor getPluginImage(String fileName) {
		URL path = getBundle().getEntry("/");//$NON-NLS-1$
		URL fullPathString = null;
		try {
			fullPathString = new URL(path, fileName);
			return ImageDescriptor.createFromURL(fullPathString);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public Image getImage(String key) {
		if ((key == null) || (key.length() == 0)) {
			return null;
		}
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			initializeImageRegistry();
		}
		Image image = null;
		try {
			image = imageRegistry.get(key);
		} catch (Throwable t) {
			MessageManager.addException(t, MessageManager.LEVEL_PRODUCTION);
		}
		return image;
	}

	public Font getFont(String key) {
		if ((key == null) || (key.length() == 0)) {
			return null;
		}
		if (fontRegistry == null) {
			fontRegistry = new FontRegistry();
			initializeFontRegistry();
		}
		return fontRegistry.get(key);
	}

	public Color getColor(String key) {
		if ((key == null) || (key.length() == 0)) {
			return null;
		}
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			initializeColorRegistry();
		}
		return colorRegistry.get(key);
	}

	public ImageDescriptor getImageDescriptor(String key) {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
			initializeImageRegistry();
		}
		ImageDescriptor image = (ImageDescriptor) imageDescriptorRegistry.get(key);
		return image;
	}

	public Shell getPluginShell() {
		if (this.getWorkbench() != null) {
			if (this.getWorkbench().getActiveWorkbenchWindow() != null) {
				return this.getWorkbench().getActiveWorkbenchWindow().getShell();
			}
		}
		return null;
	}

	public Image getCompositeImage(String key, String decoKey) {
		String id = key.concat("_").concat(decoKey); //$NON-NLS-1$
		getCompositeImageDescriptor(key, decoKey);
		return getImage(id);
	}

	public Image getCompositeImage(Image image, String decoKey) {
		ImageDescriptor imgd = getCompositeImageDescriptor(image, decoKey);
		if (imgd != null)
			return imgd.createImage();
		return null;
	}

	public String getVersion() {
		return (String) getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION);
	}


	public ImageDescriptor getCompositeImageDescriptor(String key, String decoKey) {
		String id = key.concat("_").concat(decoKey); //$NON-NLS-1$
		DecoratorImageDescriptor deco = (DecoratorImageDescriptor) imageDescriptorRegistry.get(id);
		if (deco == null) {
			ImageDescriptor main = getImageDescriptor(key);
			ImageDescriptor overlay = getImageDescriptor(decoKey);
			if ((main != null) && (overlay != null)) {
				deco = new DecoratorImageDescriptor(main.getImageData(), overlay.getImageData());
				imageDescriptorRegistry.put(id, deco);
				Image imgd = deco.createImage();
				imageRegistry.put(id, imgd);
			}
		}
		return deco;
	}

	public ImageDescriptor getCompositeImageDescriptor(Image base, String decoKey) {
		ImageDescriptor overlay = getImageDescriptor(decoKey);
		DecoratorImageDescriptor deco = new DecoratorImageDescriptor(base.getImageData(), overlay.getImageData());
		return deco;
	}

	public IShellProvider getShellProvider() {
		return shellProvider;
	}

	protected void initializeFontRegistry() {
		// Do nothing
	}

	protected void initializeColorRegistry() {
		// Do nothing
	}
}