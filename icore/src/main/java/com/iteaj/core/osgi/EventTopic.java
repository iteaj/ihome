package com.iteaj.core.osgi;

/**
 * create time: 2018/3/10
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public interface EventTopic {

    /**
     * 业务层模块: {@link org.springframework.context.ApplicationContext}服务的创建和销毁的事件主题
     */
    String SERVICE_CONTEXT = "SERVICE-CONTEXT-TOPIC";
}
