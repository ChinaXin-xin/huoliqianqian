package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * SN入库，对应的实体类，兼容之前php的数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_pos_terminal")
public class SysPosTerminal {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid; // 持有Id

    @TableField(exist = false)
    private String phone;  //根据uid查出来的

    private String machineNo; // 编号

    private Date createTime; // 添加时间

    private Date updateTime; // 修改时间

    private Date endTime;

    private Byte ver; // 1:1.0;2:2.0

    private String type; // 类型：0 删除 1入库 2 出库 3绑定 4换机 5解绑 6转移出 7转移入

    private Byte status; // 1解绑审核中 2.解绑审核通过

    private String clazz; // 机具类型

    private Byte huanjiStatu; // 换机 0 未审核 1审核

    private Byte isHuabo; // 是否划拨 1未划拨

    private String classType; // 机具类型值 1天喻 2锦弘霖

    private String merchantName; //商户名

    private String who;  //pos机所属人的真实姓名

    private String merchantId;  //商户号

    private String idCard;   //身份证

    private String simCharge; // 流量费

    public Date getCreateTime() {
        if (this.createTime == null)
            new Date();
        return createTime;
    }
}
