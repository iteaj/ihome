package com.ihome.web.layout.event;

import com.iteaj.core.osgi.spring.web.OsgiXmlWebApplicationContext;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Arrays;
import java.util.Collection;

/**
 * create time: 2018/3/9
 *  用来监听业务层的Spring Application Context的事件的处理器
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class ServiceApplicationContextEventHandler implements EventHandler {

    private ServletHolder holder;
    private BundleContext context;
    private ServletContextHandler servletContext;
    private static String URL_PATTERN = "/layout/*";
    private ApplicationContext serviceApplicationContext;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String SERVLET_NAME = "LayoutDispatcherServlet";

    public ServiceApplicationContextEventHandler(BundleContext context
            , ServletContextHandler servletContext) {
        this.context = context;
        this.servletContext = servletContext;
    }

    @Override
    public void handleEvent(Event event) {

        int eventType = (int)event.getProperty(EventConstants.EVENT);
        switch (eventType){
                //bundle停止事件
            case Bundle.STARTING:
                dealApplicationContextRegisterEvent(eventType);
                break;
                //bundle
            case Bundle.STOPPING:
                dealApplicationContextUnRegisterEvent(eventType);
                break;
        }
    }

    /**
     * Spring Application Context服務解除注冊的時候觸發的事件
     * @param eventType 事件类型  如果是{@code null}那就说明次方法非事件调用,而是单纯的普通方法调用
     */
    public void dealApplicationContextUnRegisterEvent(Integer eventType) {
        try {
            if(servletContext != null) {
                ServletHandler servletHandler = servletContext.getServletHandler();
                ServletMapping[] servletMappings = servletHandler.getServletMappings();
                ServletHolder[] oldHolders = servletHandler.getServlets();
                if(null != servletMappings && holder != null) {
                    //移除掉名为{@code SERVLET_NAME}的ServletMapping 并更新ServletMapping
                    ServletMapping[] mappings = Arrays.asList(servletMappings).stream()
                            .filter(item -> !SERVLET_NAME.equals(item.getServletName()))
                            .toArray(value -> new ServletMapping[value]);
                    servletHandler.setServletMappings(mappings);

                    //移除掉名为{@code SERVLET_NAME}的ServletHolder 并更新Servlet
                    ServletHolder[] newHolders = Arrays.asList(oldHolders).stream()
                            .filter(item -> item != holder)
                            .toArray(value -> new ServletHolder[value]);
                    servletHandler.setServlets(newHolders);

                    //关闭DispatcherServlet
                    holder.stop();
                }
            }
        } catch (Exception e) { //异常不向外传递,否则此事件处理器会被加入黑名单
            logger.error("Spring ApplicationContext的服务的解除注册事件异常", e);
        } finally {
            /**
             * 事件类型存在说明是业务层{@link com.ihome.ServiceModuleActivator#stop(BundleContext)}正在关闭<br>
             *     那么{@code serviceApplicationContext}也必须关闭
             */
            if(null != eventType) {
                //关闭父应用上下文
                if (serviceApplicationContext instanceof ConfigurableApplicationContext)
                    if (((ConfigurableApplicationContext) serviceApplicationContext).isActive())
                        ((ConfigurableApplicationContext) serviceApplicationContext).close();
            }
        }
    }

    public void dealApplicationContextRegisterEvent(Integer eventType) {
        try {
            Collection<ServiceReference<ApplicationContext>> contextServiceReferences = context
                    .getServiceReferences(ApplicationContext.class, "(module=iservice)");
            if(null != contextServiceReferences && contextServiceReferences.size()>0) {

                ServiceReference<ApplicationContext> contextServiceReference = contextServiceReferences
                        .stream().findFirst().get();
                serviceApplicationContext = context.getService(contextServiceReference);
            }

            //如果为空则将停止当前Bundle
            if(serviceApplicationContext == null) {
                throw new IllegalStateException("Spring ApplicationContext未注册成服务, 当前Bundle启动失败");
            }

            ServletHandler servletHandler = servletContext.getServletHandler();
            if(null == servletHandler.getServlet(SERVLET_NAME)) {
                OsgiXmlWebApplicationContext webApplicationContext =
                        new OsgiXmlWebApplicationContext(context);

                //设置父容器为Service层操作业务和数据库的Spring容器
                webApplicationContext.setParent(serviceApplicationContext);
                webApplicationContext.setConfigLocation("classpath:config/layout-spring.xml");

                //往ServletContextHandle加入SpringMvc的Servlet 开始向外提供路径为URL_PATTERN的Http接口服务
                DispatcherServlet servlet = new DispatcherServlet(webApplicationContext);
                holder = new ServletHolder(SERVLET_NAME, servlet);
                servletContext.addServlet(holder, URL_PATTERN);
            }
        } catch (Exception e) { //异常不向外传递,否则此事件处理器会被加入黑名单
            if(eventType == null) throw new IllegalStateException(e);
            logger.error("Spring ApplicationContext的服务的注册事件异常", e);
        }
    }
}
