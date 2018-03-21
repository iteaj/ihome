package com.ihome.web.socket;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create time: 2018/3/12
 *
 * @author iteaj
 * @version 1.0
 * @since 1.8
 */
public class EchoWebSocket extends WebSocketAdapter{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onWebSocketText(String s) {
        if(isConnected())
            getRemote().sendStringByFuture(s);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        logger.error("WebSocket Exception：{}",cause.getMessage(), cause);
    }
}
