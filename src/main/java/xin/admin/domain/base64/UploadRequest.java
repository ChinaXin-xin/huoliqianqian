package xin.admin.domain.base64;

import lombok.Data;

/**
 * 用户接收base64传输的文件
 */
@Data
public class UploadRequest {
    private String base64File;
    private String fileName;
}