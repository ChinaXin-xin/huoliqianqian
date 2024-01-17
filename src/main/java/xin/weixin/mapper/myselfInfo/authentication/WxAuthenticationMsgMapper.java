package xin.weixin.mapper.myselfInfo.authentication;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xin.weixin.domain.myselfInfo.authentication.WxAuthenticationMsg;

@Mapper
public interface WxAuthenticationMsgMapper extends BaseMapper<WxAuthenticationMsg> {

}
