package xin.zhongFu.model;

import lombok.Data;

import java.util.List;

@Data
public class TransNotifyDTO {
	 	private String ConfigAgentId;
	    
	    private String sendBatchNo;
	   
	    private String sendTime;
	   
	    private int sendNum;
	    
	    private String transDate;
	   
	    private String dataType;
	    
	    private List dataList;

}
