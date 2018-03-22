package com.iteaj.core.osgi.spring.web;

import com.iteaj.core.osgi.spring.OsgiBundleResourceLoader;
import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.IOException;

public class OsgiXmlWebApplicationContext extends XmlWebApplicationContext {

    private BundleContext bundleContext;
    private OsgiBundleResourceLoader resourceLoader;

    public OsgiXmlWebApplicationContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.resourceLoader = new OsgiBundleResourceLoader(bundleContext.getBundle());
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(getEnvironment());
        beanDefinitionReader.setResourceLoader(resourceLoader);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(resourceLoader));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    @Override
    public Resource getResource(String location) {
        return resourceLoader.getResource(location);
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return resourceLoader.getClassLoader();
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
