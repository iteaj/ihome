package com.ihome.web.socket;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * create time: 2018/3/13
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class FrameWebSocketCreator implements WebSocketCreator {

    private WebSocketListener echoWebSocket = new EchoWebSocket();

    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest
            , ServletUpgradeResponse servletUpgradeResponse) {

        String requestPath = servletUpgradeRequest
                .getHttpServletRequest().getRequestURI();
        if("/frame/socket".equals(requestPath))
            return echoWebSocket;

        throw new WebSocketException("找不到于当前路径对应的SocketListener");
    }
}
