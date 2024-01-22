package xin.admin.controller.sse;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import xin.admin.service.sse.impl.NotificationSSEServiceImpl;

@RestController
@RequestMapping("/admin/homePage")
public class NotificationSSEController {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // 注意：如果您计划支持多个客户端，您可能需要一个SseEmitter的集合而不是单个实例。
    private SseEmitter emitter = new SseEmitter(24L * 60 * 60 * 1000);

    @CrossOrigin
    @RequestMapping(value = "/notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        sendNotification(); // 当客户端连接时立即发送通知
        return emitter;
    }

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void heartbeat() {
        sendNotification();
    }

    // B函数：负责SSE发送
    public void sendNotification() {
        executorService.execute(() -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .data(JSONObject.toJSONString(NotificationSSEServiceImpl.data)));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }
}

