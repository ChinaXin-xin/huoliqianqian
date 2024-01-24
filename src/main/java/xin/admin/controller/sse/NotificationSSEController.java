package xin.admin.controller.sse;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import xin.admin.domain.sse.NotificationSSE;
import xin.admin.mapper.fundsFlowManagement.CommercialTenantOrderZFMapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/admin/homePage")
public class NotificationSSEController {

    // 线程池，让sse异步操作
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // 使用Map来存储每个客户端的SseEmitter
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 要发送的数据
    public static NotificationSSE data = new NotificationSSE();

    @Autowired
    CommercialTenantOrderZFMapper commercialTenantOrderZFMapper;

    @CrossOrigin
    @RequestMapping(value = "/notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(HttpServletRequest request) {
        String clientId = getClientId(request);
        SseEmitter emitter = new SseEmitter(24L * 60 * 60 * 1000);

        emitters.put(clientId, emitter);

        emitter.onCompletion(() -> emitters.remove(clientId));
        emitter.onTimeout(() -> emitters.remove(clientId));
        emitter.onError((e) -> emitters.remove(clientId));

        sendNotification(); // 当客户端连接时立即发送通知
        return emitter;
    }

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void heartbeat() {
        sendNotification();
    }

    /**
     * 每天凌晨自动更新每日交易信息
     * 这是UTC时间，北京比UTC快8个小时
     */
    @Scheduled(cron = "0 0 16 * * ?")
    public void updateTodayDealMsg() {
        // 设置今日交易金额
        BigDecimal sumAmountToday = commercialTenantOrderZFMapper.sumAmountToday();
        if (sumAmountToday == null || sumAmountToday.equals(BigDecimal.ZERO)) {
            NotificationSSEController.data.setTodayTransactionAmount(BigDecimal.ZERO);
        } else {
            NotificationSSEController.data.setTodayTransactionAmount(sumAmountToday.divide(new BigDecimal("100")));
        }
        // 设置今日交易笔数
        NotificationSSEController.data.setTodayTransactionCount(commercialTenantOrderZFMapper.sumCountToday().intValue());
    }


    // B函数：负责SSE发送
    public void sendNotification() {
        emitters.forEach((clientId, emitter) -> {
            executorService.execute(() -> {
                try {
                    emitter.send(SseEmitter.event()
                            .id(String.valueOf(System.currentTimeMillis()))
                            .data(JSONObject.toJSONString(data)));
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            });
        });
    }

    // 生成或获取客户端ID
    private String getClientId(HttpServletRequest request) {
        // 这里可以根据实际情况生成唯一的客户端ID
        return request.getSession(true).getId();
    }
}

