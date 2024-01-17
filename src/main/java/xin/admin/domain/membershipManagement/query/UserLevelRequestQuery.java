package xin.admin.domain.membershipManagement.query;

import lombok.Data;
import xin.admin.domain.UserLevel;

import java.util.List;

@Data
public class UserLevelRequestQuery {
    private Integer pageNumber;  //页码
    private Integer quantity;    //每页多少条
    private Integer count;       //总共多少条
    private List<UserLevel> userList; //返回的查询结果
    private Long queryUserLevelId;    //要查询的id
}
