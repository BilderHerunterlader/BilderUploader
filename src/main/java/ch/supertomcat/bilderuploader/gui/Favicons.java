package ch.supertomcat.bilderuploader.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.supertomcatutils.gui.Icons;
import net.sf.image4j.codec.ico.ICODecoder;

/**
 * Class which contains a list of icon resources
 */
public final class Favicons {
	/**
	 * Logger for this class
	 */
	private static Logger logger = LoggerFactory.getLogger(Favicons.class);

	/**
	 * Resources
	 */
	private static Map<String, Image> iconCache = new HashMap<>();

	/**
	 * Constructor
	 */
	private Favicons() {
	}

	/**
	 * Returns an ImageIcon for given file
	 * 
	 * @param file File
	 * @param size Size
	 * @return Favicon
	 */
	public static synchronized ImageIcon getFaviconIcon(File file, int size) {
		return new ImageIcon(getFaviconImage(file, size));
	}

	/**
	 * Returns an Image for given file
	 * 
	 * @param file File
	 * @param size Size
	 * @return Favicon
	 */
	public static synchronized Image getFaviconImage(File file, int size) {
		Image image = iconCache.get(file.getAbsolutePath());
		if (image == null) {
			image = loadFaviconImage(file, size);
			if (image != null) {
				iconCache.put(file.getAbsolutePath(), image);
			} else {
				logger.error("Could not load image: {}", file.getAbsolutePath());
			}
		}
		return image;
	}

	private static Image loadFaviconImage(File file, int size) {
		try {
			List<BufferedImage> images = ICODecoder.read(file);
			BufferedImage bestFit = null;
			for (BufferedImage image : images) {
				if (image.getWidth() == size && image.getHeight() == size) {
					return image;
				} else if (image.getHeight() == size) {
					bestFit = image;
					break;
				}

				if (bestFit == null) {
					bestFit = image;
				} else if (image.getHeight() > size && image.getHeight() < bestFit.getHeight()) {
					bestFit = image;
				}
			}

			if (bestFit != null) {
				return resizeImage(bestFit, size);
			}

			return Icons.getDummyImage(size);
		} catch (Exception e) {
			logger.error("Could not load image: {}", file.getAbsolutePath(), e);
			return Icons.getDummyImage(size);
		}
	}

	/**
	 * Resize Image
	 * 
	 * @param originalImage Original Image
	 * @param size Size
	 * @return Resized Image
	 */
	private static Image resizeImage(BufferedImage originalImage, int size) {
		return originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);
	}
}
