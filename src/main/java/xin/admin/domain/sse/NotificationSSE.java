package xin.admin.domain.sse;

import lombok.Data;

/**
 * 首页的sse通知的载体
 */
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NotificationSSE {
    private Integer activeMachinesCount; // 已激活机具数量
    private Integer inactiveMachinesCount; // 未激活机具数量
    private BigDecimal historicalTransactionVolume; // 历史机具交易流水金额
    private Integer historicalTransactionCount; // 历史交易笔数量
    private Integer currentVisitorsCount; // 本站多少人 -
    private BigDecimal todayTransactionAmount; // 今日交易金额
    private Integer todayTransactionCount; // 今日交易笔数 -
}

