package com.ihome.web.frame;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * create time: 2018/3/7
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class ApplicationContextTracker extends ServiceTracker {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ApplicationContextTracker(BundleContext context, ServiceReference reference) {
        super(context, reference, null);
    }

    @Override
    public Object addingService(ServiceReference reference) {
        return super.addingService(reference);
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
        logger.warn("服务监听：modified service");
        super.modifiedService(reference, service);
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        super.removedService(reference, service);
    }
}
