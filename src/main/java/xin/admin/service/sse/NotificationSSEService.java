package xin.admin.service.sse;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Sinks;
import xin.admin.domain.sse.NotificationSSE;

public interface NotificationSSEService {
    NotificationSSE getUpdatedData();
}
