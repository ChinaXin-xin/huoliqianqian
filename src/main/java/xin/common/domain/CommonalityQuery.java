package xin.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonalityQuery<T> {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Long count;       //总共多少条
    private T query;             //查询额条件
    private List<T> resultList;  //返回的查询结果
    private Date startTime;      //查询的通用时间
    private Date endTime;        //查询结束时间
}
