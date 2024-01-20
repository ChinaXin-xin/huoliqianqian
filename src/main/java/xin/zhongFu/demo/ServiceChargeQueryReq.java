package xin.zhongFu.demo;

import lombok.Data;
import xin.zhongFu.model.req.BaseReq;

@Data
public class ServiceChargeQueryReq extends BaseReq {

    private String token;

    private String traceNo;

    private String optNo;
}
