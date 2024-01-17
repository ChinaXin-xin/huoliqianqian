package xin.h5.domain.myselfMachine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sn与所属人
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineSnBelongUser {

    //机器的SN
    private String sn;

    //SN所属的人
    private String name;
}
