package com.ihome.web.socket;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * create time: 2018/3/11
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class FrameWebSocketHandle extends WebSocketHandler {

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(20000);
        webSocketServletFactory.setCreator(new FrameWebSocketCreator());
        webSocketServletFactory.getPolicy().setMaxTextMessageSize(1024);
    }
}
