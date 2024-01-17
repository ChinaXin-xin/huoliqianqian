package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户绑机通知，用户刷pos机上用身份中和银行卡激活的时候，中付那边的通知
 */
@Data
@TableName("bind_machine_inform_zf")
public class BindMachineInformZF {
    // 商户直属机构号
    private String agentId;

    // 商户号
    private String merchantId;

    // 终端号
    private String termId;

    // 终端SN
    private String termSn;

    // 终端型号
    private String termModel;

    // 助贷通版本号（可选字段）
    private String version;
}
