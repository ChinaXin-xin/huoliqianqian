package xin.zhongFu.model.req.responseDomain;

import lombok.Data;

import java.util.List;

/**
 * 业务数据。
 */
@Data
public class ResponseDataZF {
    private String agentId; // 服务商编号
    private String reqFlowNo; // 入网请求流水号
    private String merchId; // 商户号
    private String merchName; // 商户名称
    private String status; // 状态(0:无效,1:有效,X:注销)
    private String contactsName; // 联系人姓名
    private String contactsMobile; // 联系人手机号(遮蔽文)
    private String idCard; // 身份证号码(遮蔽文)
    private String idCardDateStart; // 身份证有效期开始日期
    private String idCardDateEnd; // 身份证有效期结束日期
    private String provinceName; // 省份名称
    private String provinceCode; // 省份编码
    private String cityName; // 市名称
    private String cityCode; // 市编码
    private String countyName; // 区县名称
    private String countyCode; // 区县编码
    private String busiAddr; // 居住地址
    //private List<ImageInfo> imgList; // 图片信息列表
}
