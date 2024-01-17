package xin.h5.domain.myselfCommercialTenant.query;

import lombok.Data;
import xin.h5.domain.myselfCommercialTenant.DealAndPosMachineMsg;

import java.util.List;

/**
 * 我的商户中显示信息的查询载体
 */
@Data
public class DealAndPosMachineMsgRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条

    //排序类型，默认为4：1、根据交易额降序，2：根据交易额升序，3：根据激活时间降序，4：根据激活时间升序
    private Integer sort = 1;
    private DealAndPosMachineMsg dealAndPosMachineMsg;
    private List<DealAndPosMachineMsg> resultList;
}
