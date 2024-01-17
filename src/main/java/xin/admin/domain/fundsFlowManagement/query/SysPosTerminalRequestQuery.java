package xin.admin.domain.fundsFlowManagement.query;

import lombok.Data;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;

import java.util.List;

@Data
public class SysPosTerminalRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private SysPosTerminal sysPosTerminal;
    private List<SysPosTerminal> resultList; //返回的查询结果
}
