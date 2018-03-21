package com.ihome.web.frame;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.Hashtable;

public class WebFrameModuleActivator implements BundleActivator {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void start(BundleContext context) throws Exception {
        String property = System.getProperty("jetty.home.bundle");
        System.out.println(property);
//        System.setProperty("jetty.home.bundle", "com.iteaj.iweb-frame");
//        System.setProperty("jetty.etc.config.urls", "etc/jetty.xml");
//        WebAppContext webapp = new WebAppContext();
//        Dictionary props = new Hashtable();
//        props.put("war",".");
//        props.put("contextPath","/acme");
//        props.put("managedServerName", "ihome.frame");
//        context.registerService(ContextHandler.class.getName(),webapp,props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
