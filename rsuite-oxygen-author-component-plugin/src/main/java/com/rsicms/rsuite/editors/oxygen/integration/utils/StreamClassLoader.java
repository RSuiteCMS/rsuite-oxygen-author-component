package com.rsicms.rsuite.editors.oxygen.integration.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.reallysi.rsuite.api.extensions.Plugin;

public class StreamClassLoader extends ClassLoader {

	private Plugin plugin;
	
	private ClassLoader parent;
	
	private ClassLoader oxygenPluginCL;

	public StreamClassLoader(Plugin plugin, ClassLoader customPluginCL, ClassLoader oxygenPluginCL) {
		super(customPluginCL);
		this.parent = customPluginCL;
		this.plugin = plugin;
		this.oxygenPluginCL = oxygenPluginCL;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Class loadClass(String name, boolean resolve)
			throws ClassNotFoundException {

		Class c = findLoadedClass(name);
		if (c == null) {
			try {				
				c = parent.loadClass(name);
			} catch (Throwable e) {
				// Ignore these
			}
		}
		
		if (c == null) {
			try {				
				c = oxygenPluginCL.loadClass(name);
			} catch (Throwable e) {
				// Ignore these
			}
		}

		if (c == null) {
			String filename = name.replace('.', '/') + ".class";

			try {
				byte data[] = loadClassData(filename);

				c = defineClass(name, data, 0, data.length);
				if (c == null)
					throw new ClassNotFoundException(name);

			} catch (IOException e) {
				throw new ClassNotFoundException("Error reading file: "
						+ filename);
			}
		}

		// Resolve class definition if appropriate
		if (resolve)
			resolveClass(c);
		// Return class just created

		return c;
	}

	private byte[] loadClassData(String filename) throws IOException {

		InputStream classIs = plugin.getResourceAsStream(filename);

		if (classIs == null){
			throw new IOException("Unable to find " + filename);
		}
		
		byte[] bytes = IOUtils.toByteArray(classIs);

		classIs.close();

		return bytes;
	}
}