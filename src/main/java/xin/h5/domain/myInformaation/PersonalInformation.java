package xin.h5.domain.myInformaation;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 个人信息的载体
 */
@Data
public class PersonalInformation {

    //每天签到的积分
    public static final Float everyDaySignPoints = 10f;

    //头像 -
    private String icon;

    //昵称 -
    private String nickname;

    //我的邀请码 -
    private String myInvitationCode;

    //等级 -
    private String level;

    //是否实名 -
    private Boolean authentication;

    //历史的收益
    private BigDecimal historyMoney;

    //今天的收益
    private BigDecimal todayMonty;

    //昨天的收益
    private BigDecimal yesterday;

    //要求人的手机号，就是上级的手机号，userName -
    private String superiorPhone;

    //我的积分
    private Float myPoints;

    //签到的天数
    private Integer signInDay;

    //实名认证审核状态，0未审核，1审核通过，2审核拒绝
    private Integer authenticationStatus;

}
