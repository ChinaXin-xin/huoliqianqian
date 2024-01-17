package xin.h5.domain.sms;

import lombok.Data;
import xin.common.enmu.SmsType;

@Data
public class Sms {
    private String userName;
    private String code;
    private SmsType smsType;
}
