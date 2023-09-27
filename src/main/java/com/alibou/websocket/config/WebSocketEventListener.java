package com.alibou.websocket.config;

import com.alibou.websocket.chat.ChatMessage;
import com.alibou.websocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final SimpMessageSendingOperations messagingTemplate = null;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			Logger logger = Logger.getLogger(WebSocketEventListener.class.getName());
			logger.info("user disconnected: {}" + username);
			ChatMessage chatMessage = new ChatMessage(MessageType.LEAVE, username);
			messagingTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}

}
