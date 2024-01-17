package xin.zhongFu.model.req.responseDomain;

import lombok.Data;

/**
 * 图片信息。
 */
@Data
public class ImageInfo {
    private String img; // 图片base64编码字符串
    private String imgSuffix; // 图片后缀(png/jpg/jpeg)
    private String imgType; // 图片类型(2:身份证正面图片,3:身份证反面图片)
}
