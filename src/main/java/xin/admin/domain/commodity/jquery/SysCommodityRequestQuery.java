package xin.admin.domain.commodity.jquery;

import lombok.Data;
import xin.admin.domain.commodity.SysCommodity;

import java.util.List;

@Data
public class SysCommodityRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private String category;     //商品所在的分层名称，xxx/yyy/ 就查询xxx/yyy开始的第pageNumber页，quantity条
    private List<SysCommodity> resultList; //返回的查询结果
}
