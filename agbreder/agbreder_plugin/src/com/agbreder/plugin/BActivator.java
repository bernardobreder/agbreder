package com.agbreder.plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.agbreder.plugin.element.ElementManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class BActivator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "BrederPluginEditor";

	private static BActivator plugin;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		ElementManager.getInstance();
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		try {
			URL installURL = BActivator.getDefault().getBundle().getEntry("/icons/");
			URL url = new URL(installURL, path);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	public static InputStream getTemplate(String path) {
		try {
			URL installURL = BActivator.getDefault().getBundle().getEntry("/templates/");
			URL url = new URL(installURL, path);
			return url.openStream();
		} catch (Exception e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	public static InputStream getTemplate(String path, Map<String, String> map) {
		InputStream input = getTemplate(path);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			byte[] bytes = new byte[1024];
			while (true) {
				int n = input.read(bytes);
				if (n == -1)
					break;
				output.write(bytes, 0, n);
			}
			bytes = output.toByteArray();
			output = new ByteArrayOutputStream();
			for (int n = 0; n < bytes.length; n++) {
				char c = (char) bytes[n];
				if (c == '|') {
					StringBuilder sb = new StringBuilder();
					while (true) {
						if (n++ == bytes.length) {
							throw new EOFException();
						}
						c = (char) bytes[n];
						if (c == '|')
							break;
						sb.append(c);
					}
					String value = map.get(sb.toString());
					if (value != null) {
						output.write(value.getBytes());
					} else {
						output.write("?".getBytes());
					}
				} else {
					output.write(c);
				}
			}
		} catch (IOException e) {
		}
		return new ByteArrayInputStream(output.toByteArray());
	}

}
