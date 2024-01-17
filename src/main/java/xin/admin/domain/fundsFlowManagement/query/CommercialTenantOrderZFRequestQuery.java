package xin.admin.domain.fundsFlowManagement.query;

import lombok.Data;
import xin.admin.domain.fundsFlowManagement.CommercialTenantOrderZF;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CommercialTenantOrderZFRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private BigDecimal money;       //一共多少钱
    private CommercialTenantOrderZF commercialTenantOrderZF;
    private List<CommercialTenantOrderZF> resultList; //返回的查询结果
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间

}
