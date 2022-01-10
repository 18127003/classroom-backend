package com.example.demo.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class UserInterceptor implements ChannelInterceptor {

    public static final String WS_CONNECTION_HEADER_USER = "username";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {

                Object name = ((Map) raw).get(WS_CONNECTION_HEADER_USER);
                if (Collection.class.isAssignableFrom(name.getClass())) {
                    var userId = ((LinkedList<String>) name).get(0);
                    accessor.setUser(new StompPrincipal(userId));
                }
            }

        }
        return message;
    }
}
