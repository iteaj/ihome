package com.ihome;

import com.iteaj.core.osgi.EventTopic;
import com.iteaj.core.osgi.spring.OsgiClassPathXmlApplicationContext;
import org.osgi.framework.*;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Dao层Osgi Bundle Activator
 */
public class ServiceModuleActivator implements BundleActivator {
    private Event event;
    private EventAdmin eventAdmin;
    private Map<String, Object> staring = new HashMap();
    private Map<String, Object> stopping = new HashMap();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ServiceRegistration<ApplicationContext> registration;
    private OsgiClassPathXmlApplicationContext applicationContext;

    public ServiceModuleActivator() {


    }

    public void start(BundleContext bundleContext) throws Exception {
        try {
            applicationContext = new OsgiClassPathXmlApplicationContext
                    ("/config/iservice-spring.xml", bundleContext);
            Dictionary<String, String> properties = new Hashtable<>();
            properties.put("module", "iservice");

            registration = bundleContext.registerService
                    (ApplicationContext.class, applicationContext, properties);

            ServiceReference<EventAdmin> eventAdminReference =
                    bundleContext.getServiceReference(EventAdmin.class);
            eventAdmin = bundleContext.getService(eventAdminReference);

            staring.put(EventConstants.EVENT, Bundle.STARTING);
            staring.put(EventConstants.BUNDLE_SYMBOLICNAME
                    , bundleContext.getBundle().getSymbolicName());
            eventAdmin.postEvent(new Event(EventTopic.SERVICE_CONTEXT, staring));
        } catch (Exception e) {
            logger.error("未知异常", e);
            throw e;
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        try {
            if(registration != null) registration.unregister();
            if(eventAdmin != null) {//发送一个Sprint ApplicationContext停止的事件
                stopping.put(EventConstants.BUNDLE_SYMBOLICNAME
                        , bundleContext.getBundle().getSymbolicName());
                stopping.put(EventConstants.EVENT, Bundle.STOPPING);
                eventAdmin.postEvent(new Event(EventTopic.SERVICE_CONTEXT, stopping));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
