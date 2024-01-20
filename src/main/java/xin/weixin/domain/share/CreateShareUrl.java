package xin.weixin.domain.share;

import lombok.Data;

@Data
public class CreateShareUrl {
    // 商品id
    private Integer cid;

    // 分享的url
    private String shareUrl;
}
