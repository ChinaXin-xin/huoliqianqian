package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 中付商户服务费记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_service_charge_history")
public class SysServiceChargeHistory {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer snId;

    private String traceNo;

    private String optNo;
}