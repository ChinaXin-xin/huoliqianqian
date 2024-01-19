package xin.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户表(User)实体类
 *
 * @author 三更
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String openId;

    /**
     * 登录名，登录名也可以是手机号phone
     */
    private String userName;

    /**
     * 用户的jwt
     */
    private String jwt;

    /**
     * 微信的session，先用着
     */
    private String sessionKey;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号，登录
     */
    private String phone;

    /**
     * 是否是管理员
     */
    private Boolean isAdmin;

    /**
     * 别人的邀请码
     */
    private String invitationCode;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 分润的钱
     */
    private Float shareBenefit;

    private Float activationAward; // 激活奖
    private Float activityAward;   // 活动奖
    private Float achievementAward; // 达标奖
    private Float teamAward;        // 团队奖
    private Float cheerAward;       // 推荐奖

    /**
     * 头像地址
     */
    private String icon;

    /**
     * 用不到
     * 自己已经激活的sn码，-222-333-444，这种格式
     * 一个人能有三台SN
     */
    private String sn_list;

    /**
     * 自己的邀请码
     */
    @TableField("my_invitation_code")
    private String myInvitationCode;

    /**
     * smsCode，登录和注册验证码
     */
    @TableField(exist = false)
    private String code;

    /**
     * 我的上级，账号
     */
    @TableField(exist = false)
    private String superiorPhone;

    /**
     * 小伙伴姓名
     */
    private String name;

    /**
     * 伙伴编号
     */
    private String myselfNumber;

    /**
     * 伙伴级别
     */
    private String Level;

    /**
     * 手动级别
     */
    private String manualLevel;

    /**
     * 手动级别的到期日期
     */
    private Date expireDate;

    /**
     * 手动级别的开始日期
     */
    private Date startDate;

    /**
     * 时间，没有说明什么时间
     */
    private Date time;

    /**
     * 是否实名
     */
    private Boolean isAuthentication;

    /**
     * 推荐人姓名
     */
    private String referrerName;

    /**
     * 推荐人编号
     */
    private String referrerNumber;

    /**
     * 推荐人级别
     */
    private String referrerLevel;

    /**
     * 入库
     */
    private Integer storage;

    /**
     * 出库
     */
    private Integer stockRemoval;

    /**
     * 已激活机具数
     */
    private Integer activatedMachineryCount;

    /**
     * 未激活机具数
     */
    private Integer inactiveMachineryCount;

    /**
     * 商户转入
     */
    private Integer merchantTransferIn;

    /**
     * 商户转出
     */
    private Integer merchantTransferOut;

    /**
     * 我的积分
     */
    private Float myPoints;

    /**
     * 推荐奖
     */
    private Float referralReward;

    /**
     * 分润奖
     */
    private Float profitSharingReward;

    /**
     * 用户的交易量
     */
    private Float transactionVolume;

    /**
     * 伙伴版本，这个没用写死为2
     */
    private String version;


    /**
     * 账号状态（0正常 1冻结）
     */
    private String status;

    /**
     * 用户类型（0管理员，1普通用户）
     */
    private String userType;
    /**
     * 创建人的用户id
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;
}