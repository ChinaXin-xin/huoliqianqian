package xin.h5.service.wallet.myEnum;

public enum AwardType {
    ACTIVATION_AWARD("激活奖"),
    ACTIVITY_AWARD("活动奖"),
    ACHIEVEMENT_AWARD("达标奖"),
    TEAM_AWARD("团队奖"),
    CHEER_AWARD("加油奖"),
    REFERRAL_REWARD("推荐奖"),
    PROFIT_SHARING_REWARD("分润奖");

    private final String description;

    // 输入字符串，输出对应的值
    public static AwardType fromString(String text) {
        for (AwardType b : AwardType.values()) {
            if (b.description.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null; // 或者抛出一个异常
    }

    AwardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
