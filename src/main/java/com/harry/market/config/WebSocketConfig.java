package com.harry.market.config;

import com.harry.market.handler.WebSocketPushHandler;
import com.harry.market.interceptor.MyWebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 222100209_李炎东
 * @usage: 配置WebSocket
 */


@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {
    /**
     * 注册WebSocket处理类
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("注册WebSocket处理类");
        registry.addHandler(createWebSocketPushHandler(), "/webSocketServer")
                .addInterceptors(createHhandshakeInterceptor()).setAllowedOrigins("*");
        registry.addHandler(createWebSocketPushHandler(), "/sockjs/webSocketServer")
                .addInterceptors(createHhandshakeInterceptor()).withSockJS();
    }

    /**
     *
     * @Title: createHhandshakeInterceptor
     * @Description: 握手拦截器
     * @return
     */
    @Bean
    public HandshakeInterceptor createHhandshakeInterceptor() {
        System.out.println("握手拦截器");
        return new MyWebSocketInterceptor();
    }

    /**
     *
     * @Title: createWebSocketPushHandler
     * @Description: 处理类
     * @return
     */
    @Bean
    public WebSocketHandler createWebSocketPushHandler() {
        System.out.println("处理类");
        return new WebSocketPushHandler();
    }
}