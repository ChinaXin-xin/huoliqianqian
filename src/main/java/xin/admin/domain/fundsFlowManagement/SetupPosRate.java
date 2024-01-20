package xin.admin.domain.fundsFlowManagement;

import lombok.Data;

/**
 * 设置费率的载体
 */
@Data
public class SetupPosRate {
    // pos机的id
    private int id;

    // SIM服务费金额(元)(该参数如果传0代表不扣通信SIM费)
    private String simCharge = "60";

    // VIP会员服务费金额(元)(该参数如果传0代表不扣VIP会员费)
    private String vipCharge = "0";

    // POS服务费金额(元)(该参数如果传0代表不扣POS服务费)
    private String posCharge = "0";

    // 是否发送短信(1发送 0不发送)注:当前只允许传1发送
    private String smsSend;
}
