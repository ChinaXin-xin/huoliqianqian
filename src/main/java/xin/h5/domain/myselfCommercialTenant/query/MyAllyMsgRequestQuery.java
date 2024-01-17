package xin.h5.domain.myselfCommercialTenant.query;

import lombok.Data;
import xin.h5.domain.myselfCommercialTenant.MyAllyMsg;

import java.util.List;

@Data
public class MyAllyMsgRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条

    //排序类型，是否排序
    private Boolean sort = false;
    private MyAllyMsg myAllyMsg;
    private List<MyAllyMsg> resultList;
}
