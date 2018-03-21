package com.iteaj.core.osgi.spring;

import com.iteaj.core.io.OsgiUtils;
import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;

public class OsgiClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    private BundleContext bundleContext;
    private OsgiBundleResourceLoader resourceLoader;
    private OsgiBundleResourcePatternResolver resolver;
    public OsgiClassPathXmlApplicationContext(String configLocations, BundleContext bundleContext) throws BeansException {
        this.bundleContext = bundleContext;
        this.resourceLoader = new OsgiBundleResourceLoader(bundleContext.getBundle());
        this.resolver = new OsgiBundleResourcePatternResolver(this.resourceLoader);
        setConfigLocations(configLocations);
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(this.getEnvironment());
        beanDefinitionReader.setResourceLoader(resourceLoader);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(resourceLoader));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    @Override
    protected ResourcePatternResolver getResourcePatternResolver() {
        return resolver;
    }

    @Override
    public Resource getResource(String location) {
        Resource resource = resolver.getResource(location);
        if(!resource.exists() && location.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)){
            try {
                URL url = OsgiUtils.getLocalResource(bundleContext, location.substring(10));
                if(null != url)
                    return new UrlResource(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return resource;
    }

    @Override
    public ClassLoader getClassLoader() {
        return resourceLoader.getClassLoader();
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        return resolver.getResources(locationPattern);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        OsgiBundleResource resource = new OsgiBundleResource(bundleContext.getBundle(), path);
        return resource;
    }
}
