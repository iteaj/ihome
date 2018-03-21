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

package com.iteaj.core.io;

import com.iteaj.core.util.CompoundEnumeration;
import com.iteaj.core.util.SingleEnumeration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * Simple utils class for the IO package. This method might contain util methods
 * from other packages since it the IO package needs to be stand-alone.
 * 
 * @author Costin Leau
 * 
 */
public abstract class OsgiUtils {

	private static final String GET_BUNDLE_CONTEXT_METHOD = "getBundleContext";
	private static final String GET_CONTEXT_METHOD = "getContext";


	public static String getPlatformName(BundleContext bundleContext) {
		String vendorProperty = bundleContext.getProperty(Constants.FRAMEWORK_VENDOR);
		String frameworkVersion = bundleContext.getProperty(Constants.FRAMEWORK_VERSION);

		// get system bundle
		Bundle bundle = bundleContext.getBundle(0);
		String name = (String) bundle.getHeaders().get(Constants.BUNDLE_NAME);
		String version = (String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
		String symName = bundle.getSymbolicName();

		StringBuilder buf = new StringBuilder();
		buf.append(name);
		buf.append(" ");
		buf.append(symName);
		buf.append("|");
		buf.append(version);
		buf.append("{");
		buf.append(frameworkVersion);
		buf.append(" ");
		buf.append(vendorProperty);
		buf.append("}");

		return buf.toString();
	}

	private static boolean isPlatformVendorMatch(BundleContext bundleContext, String vendorString) {
		String vendor = bundleContext.getProperty(Constants.FRAMEWORK_VENDOR);
		if (vendor != null)
			return vendor.indexOf(vendorString) >= -1;
		return false;
	}

	private static boolean isEquinox(BundleContext bundleContext) {
		return isPlatformVendorMatch(bundleContext, "clispe");
	}

	private static boolean isKnopflerfish(BundleContext bundleContext) {
		return isPlatformVendorMatch(bundleContext, "fish");
	}

	private static boolean isFelix(BundleContext bundleContext) {
		return isPlatformVendorMatch(bundleContext, "pache");
	}

	/**
	 * Returns the underlying BundleContext for the given Bundle. This uses
	 * reflection and highly dependent of the OSGi implementation. Should not be
	 * used if OSGi 4.1 is being used.
	 * 
	 * <b>Note:</b> Identical to the util found in Spring-DM core
	 * 
	 * @param bundle OSGi bundle
	 * @return the bundle context for this bundle
	 */
	public static BundleContext getBundleContext(final Bundle bundle) {
		if (bundle == null)
			return null;

		// run into a privileged block
		if (System.getSecurityManager() != null) {
			return AccessController.doPrivileged(new PrivilegedAction<BundleContext>() {

				public BundleContext run() {
					return getBundleContextWithPrivileges(bundle);
				}
			});
		}
		else {
			return getBundleContextWithPrivileges(bundle);
		}
	}

	private static BundleContext getBundleContextWithPrivileges(final Bundle bundle) {
		// try Equinox getContext
		Method meth = ReflectionUtils.findMethod(bundle.getClass(), GET_CONTEXT_METHOD, new Class[0]);

		// fallback to getBundleContext (OSGi 4.1)
		if (meth == null)
			meth = ReflectionUtils.findMethod(bundle.getClass(), GET_BUNDLE_CONTEXT_METHOD, new Class[0]);

		final Method m = meth;

		if (meth != null) {
			ReflectionUtils.makeAccessible(meth);
			return (BundleContext) ReflectionUtils.invokeMethod(m, bundle);
		}

		// fallback to field inspection (KF and Prosyst)
		final BundleContext[] ctx = new BundleContext[1];

		ReflectionUtils.doWithFields(bundle.getClass(), new FieldCallback() {

			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				ReflectionUtils.makeAccessible(field);
				ctx[0] = (BundleContext) field.get(bundle);
			}
		}, new FieldFilter() {

			public boolean matches(Field field) {
				return BundleContext.class.isAssignableFrom(field.getType());
			}
		});

		return ctx[0];
	}

	public static Class<?> getClass(BundleContext bundleContext, String className) throws ClassNotFoundException {
		DependencyResolver resolver = new PackageAdminResolver(bundleContext);
		ImportedBundle[] bundles = resolver.getImportedBundles(bundleContext.getBundle());
		try {
			Class<?> loadClass = bundleContext
					.getBundle().loadClass(className);

			if(null != loadClass)
				return loadClass;
		} catch (ClassNotFoundException e) {
			Class<?> aClass = null;
			for(ImportedBundle item : bundles){
				try {
					aClass = item.getBundle().loadClass(className);
				} catch (ClassNotFoundException ee) {
					continue;
				}

			}

			if (aClass != null)
				return aClass;
			else
				throw new ClassNotFoundException();
		}

		return null;
	}

	public static Properties loadAllProperties(String resourceName, BundleContext bundleContext) throws IOException {
		DependencyResolver resolver = new PackageAdminResolver(bundleContext);
		ImportedBundle[] bundles = resolver.getImportedBundles(bundleContext.getBundle());

		List<Enumeration<URL>> list = new ArrayList<>();
		for (ImportedBundle item : bundles){
			Enumeration<URL> urls = item.getBundle().getResources(resourceName);
			if(null == urls) continue;
			else list.add(urls);
		}
		Properties props = new Properties();
		for(Enumeration<URL> urls : list) {
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				URLConnection con = url.openConnection();
				ResourceUtils.useCachesIfNecessary(con);
				InputStream is = con.getInputStream();
				try {
					if (resourceName.endsWith(".xml")) {
						props.loadFromXML(is);
					} else {
						props.load(is);
					}
				} finally {
					is.close();
				}
			}
		}
		return props;
	}

	public static Enumeration<URL> getResources(BundleContext bundleContext, String name) throws IOException {
		Enumeration<URL> enumeration = null;
		if(null != bundleContext)
			enumeration = bundleContext.getBundle().getResources(name);

		if(null != enumeration)
			return enumeration;

		List<Enumeration<URL>> list = getEnumerations(bundleContext, name);
		if(CollectionUtils.isEmpty(list))
			return new CompoundEnumeration(null);

		return new CompoundEnumeration(list.toArray(new Enumeration[list.size()]));
	}


	public static URL getLocalResource(BundleContext bundleContext, String name) throws Exception {
		Enumeration<URL> enumeration = bundleContext.getBundle().getResources(name);

		if(enumeration != null){
			return convertToLocalResource(bundleContext, enumeration.nextElement());
		} else {
			DependencyResolver resolver = new PackageAdminResolver(bundleContext);
			ImportedBundle[] bundles = resolver.getImportedBundles(bundleContext.getBundle());
			if(null != bundles && bundles.length>0 ){
				for (ImportedBundle item : bundles){
					Enumeration<URL> resources = item.getBundle().getResources(name);
					if(resources != null && resources.hasMoreElements())
						return resources.nextElement();
				}
			}

		}
		return null;
	}

	public static URL convertToLocalResource(BundleContext context, URL url) throws Exception {
		if(null == url) return url;

		URLConnection connection = url.openConnection();
		if(isFelix(context)) {
			Method getLocalURL = connection
					.getClass().getDeclaredMethod("getLocalURL");

			getLocalURL.setAccessible(true);
			return (URL) getLocalURL.invoke(connection);
		}

		return url;
	}

	public static URL convertToLocalResource(URL url) throws Exception {
		if(null == url) return url;

		URLConnection connection = url.openConnection();
		Method getLocalURL = connection
				.getClass().getDeclaredMethod("getLocalURL");

		getLocalURL.setAccessible(true);
		return (URL) getLocalURL.invoke(connection);
	}

	public static Enumeration<URL> getLocalResources(BundleContext bundleContext, String name) throws Exception {
		DependencyResolver resolver = new PackageAdminResolver(bundleContext);
		ImportedBundle[] bundles = resolver.getImportedBundles(bundleContext.getBundle());

		Enumeration<URL> resources = bundleContext.getBundle().getResources(name);
		if(null != resources && resources.hasMoreElements()){
			return new SingleEnumeration<>(convertToLocalResource(bundleContext, resources.nextElement()));
		}

		List<Enumeration<URL>> list = new ArrayList<>();
		if(null != bundles && bundles.length>0 ){
			for (ImportedBundle item : bundles){
				Enumeration<URL> url = item.getBundle().getResources(name);
				if(null == url) {
					continue;
				}
				else {
					while (url.hasMoreElements()){
						URL convert = convertToLocalResource(bundleContext, url.nextElement());
						list.add(new SingleEnumeration<>(convert));
					}
				}
			}
		}

		return new CompoundEnumeration(list.toArray(new Enumeration[list.size()]));
	}

	private static List<Enumeration<URL>> getEnumerations(BundleContext bundleContext, String name) throws IOException {
		List<Enumeration<URL>> list = new ArrayList<>();

		DependencyResolver resolver = new PackageAdminResolver(bundleContext);
		ImportedBundle[] bundles = resolver.getImportedBundles(bundleContext.getBundle());
		if(null != bundles && bundles.length>0 ) {
			for (ImportedBundle item : bundles) {
				Enumeration<URL> urls = item.getBundle().getResources(name);
				if (null == urls) continue;
				else list.add(urls);
			}
		}
		return list;
	}
}