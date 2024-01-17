package xin.admin.domain.membershipManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 认证审核表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_authentication_info")
public class UserAuthenticationInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 主键

    private Long uid; //绑定的用户id，sys_user表中的

    private String memberPhone; // 会员手机，与sys_user的phone关联

    private String memberName; // 姓名，与sys_user的name关联

    private String idCardNumber; // 身份证号码

    private String idCardFront; // 身份证正面图片

    private String idCardBack; // 身份证背面图片

    private String idCardHandheld; // 手持身份证图片

    private String bankCard; // 银行卡号

    private String bankBranch; // 所属支行

    private Date applicationDate; // 申请时间

    private Integer status; // 状态（例如：0未处理，1已批准，2已拒绝）

    private String rejectionReason; // 拒绝理由
}
