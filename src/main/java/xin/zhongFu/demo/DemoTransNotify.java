package xin.zhongFu.demo;



import xin.zhongFu.constants.EncodingConstants;
import xin.zhongFu.model.TransNotifyDTO;
import xin.zhongFu.model.TransNotifyDetailDTO;
import xin.zhongFu.utils.SortUtil;
import xin.zhongFu.utils.signutil.EncryptUtil;

import java.util.ArrayList;
import java.util.List;

public class DemoTransNotify {

	public static final String KEY = "1F4095456C00B9540B2FD035158E250E";
	
	public static void main (String[] args) throws Exception {
		TransNotifyDTO sendMsgDTO = new TransNotifyDTO();
        sendMsgDTO.setConfigAgentId("74800378");
        sendMsgDTO.setSendBatchNo("000283");
        sendMsgDTO.setSendTime("20231106181531");
        sendMsgDTO.setSendNum(1);
        sendMsgDTO.setTransDate("20231106");
        sendMsgDTO.setDataType("1");
        
        List<TransNotifyDetailDTO> list = new ArrayList<TransNotifyDetailDTO>();
        TransNotifyDetailDTO detail = new TransNotifyDetailDTO();
        detail.setAgentId("74800378");
        detail.setAmount("68200");
        detail.setAuthCode("367726");
        detail.setBatchNo("000009");
        detail.setCardNo("625368******5776");
        detail.setCardType("1");
        detail.setChannelMerchId("847498058120284");
        detail.setChannelMerchName("原阳县老马牛肉拉面馆");
        detail.setChannelRrn("110618668218");
        detail.setChannelTermId("15734063");
        detail.setFeeType("YN");
        detail.setInputMode("071");
        detail.setMerchLevel("3");
        detail.setMerchantId("748000000029611");
        detail.setMerchantName("个体商户左晓燕");
        detail.setMobileNo("156****7861");
        detail.setRrn("181433510762");
        detail.setSettleAmount("67941");
        detail.setSettleDate("20231107");
        detail.setSysRespCode("00");
        detail.setSysRespDesc("交易[181433510762]返回[00]:交易成功");
        detail.setSysTraceNo("001591");
        detail.setTermId("00062580");
        detail.setTermModel("H60-A");
        detail.setTermSn("28106973");
        detail.setTraceNo("001591");
        detail.setTranCode("020000");
        detail.setTranTime("20231106181433");
        
        
        list.add(detail);
        sendMsgDTO.setDataList(list);
        
        String msg = SortUtil.sort(sendMsgDTO);
        String sMsg = KEY + msg;
        System.out.println("待签名字符串:"+sMsg);
        String signStr = EncryptUtil.md5Encrypt(sMsg, EncodingConstants.UTF_8_ENCODING);
        System.out.println("签名:"+signStr);
	}
}
