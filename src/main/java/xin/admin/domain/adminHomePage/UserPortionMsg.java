package xin.admin.domain.adminHomePage;

import lombok.Data;
import xin.h5.domain.myselfMachine.MyselfMachineNum;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 用户管理->修改弹窗中的信息
 */
@Data
public class UserPortionMsg {
    private Long id;

    private String nickname;          // 昵称
    private String userName;          // 账号
    private String myInvitationCode;  // 邀请码
    private String newPassword;       // 新密码
    private String Level;             // 级别
    private Date createTime;          // 入网时间

    private Float activationAward;   // 激活奖
    private Float activityAward;     // 活动奖
    private Float achievementAward;  // 达标奖
    private Float teamAward;         // 团队奖
    private Float profitSharingReward;      // 分润奖
    private Float referralReward;    // 推荐奖
    private Float myPoints;          // 积分
    List<MyselfMachineNum> posList;    // 每个牌子pos机对应的数量

    private Boolean isAuthentication; // 是否已经认证
    private String memberPhone;       // 会员手机，与sys_user的phone关联
    private String memberName;        // 姓名，与sys_user的name关联
    private String bankCard;          // 银行卡号
    private String bankBranch;        // 所属支行

    private String relation;          // 推荐关系统
}
