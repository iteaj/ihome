package com.ihome.web.layout;

import com.ihome.web.layout.event.ServiceApplicationContextEventHandler;
import com.iteaj.core.osgi.EventTopic;
import com.iteaj.core.osgi.spring.web.OsgiXmlWebApplicationContext;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * create time: 2017/11/5
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class LayoutModuleActivator implements BundleActivator{


    private ServletContextHandler servletContext;

    private ServiceRegistration eventRegistration;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ServiceApplicationContextEventHandler contextEventHandler;

    public LayoutModuleActivator() {
        logger.error("LayoutModuleActivator 构造函数");
    }

    @Override
    public void start(BundleContext context) throws Exception {
        try {
//            if(null == servletContext) {
//                ServiceReference<ServletContextHandler> reference = context
//                        .getServiceReference(ServletContextHandler.class);
//                servletContext = context.getService(reference);
//            }
//            if(null == servletContext)
//                logger.error("bundle：{} - 描述：获取不到注册的服务 {}", ServletContextHandler.class.getSimpleName());

//            ServiceReference<ConfigurationAdmin> contextServiceReference = context
//                    .getServiceReference(ConfigurationAdmin.class);
//            ConfigurationAdmin service = context.getService(contextServiceReference);
//            service.getConfiguration()

//            Dictionary dict = new Hashtable();
//            dict.put(EventConstants.EVENT_TOPIC, new String[]{EventTopic.SERVICE_CONTEXT});
//            contextEventHandler = new ServiceApplicationContextEventHandler(context, servletContext);
//            eventRegistration = context.registerService
//                    (EventHandler.class.getName(), contextEventHandler, dict);
//
//            contextEventHandler.dealApplicationContextRegisterEvent(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }


    }

    @Override
    public void stop(BundleContext context) throws Exception {

        if(contextEventHandler != null)
            contextEventHandler.dealApplicationContextUnRegisterEvent(null);

        if(eventRegistration != null) eventRegistration.unregister();

    }
}
