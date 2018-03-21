/******************************************************************************
 * Copyright (c) 2006, 2010 VMware Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution. 
 * The Eclipse Public License is available at 
 * http://www.eclipse.org/legal/epl-v10.html and the Apache License v2.0
 * is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses. 
 * 
 * Contributors:
 *   VMware Inc.
 *****************************************************************************/

package com.iteaj.core.osgi.spring;

import com.iteaj.core.io.OsgiUtils;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * OSGi specific {@link org.springframework.core.io.ResourceLoader}
 * implementation.
 * 
 * This loader resolves paths inside an OSGi bundle using the bundle native
 * methods. Please see {@link OsgiBundleResource} javadoc for information on
 * what prefixes are supported.
 * 
 * @author Adrian Colyer
 * @author Costin Leau
 * 
 * @see Bundle
 * @see OsgiBundleResource
 * 
 */
public class OsgiBundleResourceLoader extends DefaultResourceLoader {

	private final Bundle bundle;
	private ClassLoader classLoader;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Creates a OSGi aware <code>ResourceLoader</code> using the given
	 * bundle.
	 *
	 * @param bundle OSGi <code>Bundle</code> to be used by this loader
	 * loader.
	 */
	public OsgiBundleResourceLoader(Bundle bundle) {
		this.bundle = bundle;
		this.classLoader = new OsgiBundleClassLoader();
	}

	protected Resource getResourceByPath(String path) {
		Assert.notNull(path, "Path is required");
		return new OsgiBundleResource(this.bundle, path);
	}

	public Resource getResource(String location) {
		Assert.notNull(location, "location is required");
		String path = location;
		return new OsgiBundleResource(bundle, path);
	}

	/**
	 * Returns the bundle used by this loader.
	 * 
	 * @return OSGi <code>Bundle</code> used by this resource
	 */
	public final Bundle getBundle() {
		return bundle;
	}

	@Override
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	class OsgiBundleClassLoader extends ClassLoader{

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			return OsgiUtils.getClass(bundle.getBundleContext(), name);
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			return super.loadClass(name, resolve);
		}

		@Override
		public URL getResource(String name) {
			try {
				return OsgiUtils.getLocalResource(bundle.getBundleContext(), name);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return null;
		}

		@Override
		public Enumeration<URL> getResources(String name) throws IOException {
			try {
				return OsgiUtils.getLocalResources(bundle.getBundleContext(), name);
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
	}
}
