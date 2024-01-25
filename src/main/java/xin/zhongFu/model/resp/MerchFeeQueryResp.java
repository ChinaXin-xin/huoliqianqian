package xin.zhongFu.model.resp;

import lombok.Data;

@Data
public class MerchFeeQueryResp {

    // 额外自定义的
    private Integer posId;

    private String merchId;

    private String agentId;

    private String cFeeRate;

    private String dFeeRate;

    private String dFeeMax;

    private String wechatPayFeeRate;

    private String alipayFeeRate;

    private String ycFreeFeeRate;

    private String ydFreeFeeRate;

    /**
     * D0手续费费率(%)
     */
    private String d0FeeRate;

    /**
     * 单笔提现费
     */
    private String d0SingleCashDrawal;

    private String status;

}
