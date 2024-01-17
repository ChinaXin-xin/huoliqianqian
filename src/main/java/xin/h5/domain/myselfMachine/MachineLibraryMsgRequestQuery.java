package xin.h5.domain.myselfMachine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.admin.domain.fundsFlowManagement.SysPosTerminal;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineLibraryMsgRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Long count;       //总共多少条
    private SysPosTerminal sysPosTerminal;
    private List<MachineSnBelongUser> resultList; //返回的查询结果

    //根据分页信息获取
    public List<MachineSnBelongUser> getResultListPage() {
        if (resultList == null || resultList.isEmpty() || pageNumber == null || quantity == null) {
            return Collections.emptyList();
        }

        int start = (pageNumber - 1) * quantity;
        int end = Math.min(start + quantity, resultList.size());

        if (start >= resultList.size() || start < 0) {
            return Collections.emptyList();
        }

        return resultList.subList(start, end);
    }
}
