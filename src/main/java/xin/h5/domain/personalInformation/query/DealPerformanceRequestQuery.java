package xin.h5.domain.personalInformation.query;

import lombok.Data;
import xin.h5.domain.personalInformation.DealPerformance;

import java.util.Collections;
import java.util.List;

/**
 * 用于查询自己，伙伴。个人累计的，日、月记录
 */

@Data
public class DealPerformanceRequestQuery {

    //访问自己的，伙伴的，还是历史的
    private String type;

    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条

    List<DealPerformance> resultList;  //数据

    // 分页函数
    public List<DealPerformance> getMyselfResultList() {
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
