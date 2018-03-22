package com.ihome.web.frame;

import com.ihome.web.frame.servlet.WebFrameServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
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
        ContextHandler handler = new ContextHandler();
        Dictionary props = new Hashtable();
        props.put("contextPath","/frame");
        context.registerService(ContextHandler.class.getName(),handler,props);
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }
}
