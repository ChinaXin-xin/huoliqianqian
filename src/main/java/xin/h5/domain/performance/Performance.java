package xin.h5.domain.performance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 每日的交易数据
 */
@Data
@TableName("sys_performance")
public class Performance {
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 当天的交易额
    private BigDecimal todayMoney = BigDecimal.ZERO;

    private Date createTime;

    private String termSn;

    private String termModel;
}
