package xin.admin.domain.fundsFlowManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pos_transfer_record")
public class PosTransferRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 划拨人
     */
    private String transferor;

    /**
     * 接收人
     */
    private String recipient;

    /**
     * sn码
     */
    private String sn;

    /**
     * 详情
     */
    private String details;

    /**
     * 划拨时间
     */
    private Date transferTime;
}
