package xin.h5.domain.myselfMachine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 我的机具的类型与数量
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyselfMachineNum {

    //机具类型
    private String type;

    //该类型机具的数量
    private Integer number;
}
