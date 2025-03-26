// 路径：src/main/java/com/example/websocket/GetHttpSessionConfigurator.java
package com.qwq.Statistics.controller;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(
            ServerEndpointConfig sec,
            HandshakeRequest request,
            HandshakeResponse response
    ) {
        // 从HTTP请求中获取HttpSession并存入属性
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }

    /**
     * 工具方法：从EndpointConfig中获取HttpSession
     */
    public static HttpSession getHttpSession(ServerEndpointConfig config) {
        return (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
    }
}
