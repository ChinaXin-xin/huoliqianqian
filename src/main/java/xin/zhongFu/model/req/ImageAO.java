package xin.zhongFu.model.req;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class ImageAO {
	
    @NotEmpty
    private String img;
    
    @NotEmpty
    private String imgSuffix;
    
    @NotEmpty
    private Integer imgType;
    
}
