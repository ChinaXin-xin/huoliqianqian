package xin.admin.config.webSocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import xin.admin.service.sse.impl.NotificationSSEServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatMessageHandler extends TextWebSocketHandler {

    private List<WebSocketSession> webSocketSessions = Collections.synchronizedList(new ArrayList<>());
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ChatMessageHandler() {
        // 定时任务，每3秒发送一次消息
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 创建消息
                TextMessage message = new TextMessage("777");
                // 遍历会话列表并发送消息
                synchronized (webSocketSessions) {
                    for (WebSocketSession session : webSocketSessions) {
                        if (session.isOpen()) {
                            session.sendMessage(message);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        webSocketSessions.remove(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        String modifiedMessage = message.getPayload() + "666";
        TextMessage newMessage = new TextMessage(modifiedMessage);

        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(newMessage);
            }
        }
    }
}

