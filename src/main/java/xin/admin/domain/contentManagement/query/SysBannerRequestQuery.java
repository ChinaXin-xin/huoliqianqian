package xin.admin.domain.contentManagement.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xin.admin.domain.contentManagement.SysBanner;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysBannerRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private SysBanner sysBanner;
    private List<SysBanner> resultList; //返回的查询结果
}
