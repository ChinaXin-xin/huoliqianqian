package xin.admin.domain.contentManagement.query;

import lombok.Data;
import xin.admin.domain.contentManagement.SysMsg;

import java.util.List;

@Data
public class SysMsgRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private SysMsg sysMsg;
    private List<SysMsg> resultList; //返回的查询结果
}
