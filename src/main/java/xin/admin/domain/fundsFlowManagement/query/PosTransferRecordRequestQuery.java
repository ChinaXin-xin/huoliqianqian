package xin.admin.domain.fundsFlowManagement.query;

import lombok.Data;
import xin.admin.domain.fundsFlowManagement.PosTransferRecord;

import java.util.List;

@Data
public class PosTransferRecordRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private PosTransferRecord posTransferRecord;
    private List<PosTransferRecord> resultList; //返回的查询结果
}
