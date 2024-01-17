package xin.admin.service.commonality;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UpdateOrDownloadService {

    @Value("${virtualFileDepositPath}")
    private String virtualFileDepositPath;

    @Value("${realityFileDepositPath}")
    private String realityFileDepositPath;

    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 获取文件名
        String fileName = file.getOriginalFilename();

        // 确保文件名存在并且不是.exe文件
        if (fileName != null && !fileName.toLowerCase().endsWith(".exe")) {
            fileName = System.currentTimeMillis() + fileName;
            // 检查文件大小是否合适
            if (file.getSize() <= 10 * 1024 * 1024) { // 10 MB
                // 保存文件
                try {
                    String fullPath = realityFileDepositPath + fileName;
                    file.transferTo(new File(fullPath));
                    System.out.println("公共文档已保存至：" + fullPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 公共文档上传失败，因内部错误
                    return new ResponseEntity<>("公共文档上传失败，由于内部错误", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                // 返回文件访问虚拟路径
                return new ResponseEntity<>(virtualFileDepositPath.substring(0, virtualFileDepositPath.length() - 2) + fileName, HttpStatus.OK);
            } else {
                // 公共文档大小超过10MB
                return new ResponseEntity<>("公共文档大小超过10MB", HttpStatus.BAD_REQUEST);
            }
        } else {
            // 公共文档格式不支持或为可执行文件，这是不允许的
            return new ResponseEntity<>("公共文档格式不支持或为可执行文件，这是不允许的", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param base64Data 文件base64编码
     * @param fileName   文件名称
     * @return
     */
    public ResponseEntity<String> handleFileUploadBase64(String base64Data, String fileName) {
        // 去除Base64数据前缀
        String base64File = base64Data.substring(base64Data.indexOf(",") + 1);

        byte[] fileData = Base64.decodeBase64(base64File);

        // 确保文件名存在且不是.exe文件
        if (fileName != null && !fileName.toLowerCase().endsWith(".exe")) {
            fileName = System.currentTimeMillis() + fileName;
            // 检查文件大小
            if (fileData.length <= 10 * 1024 * 1024) { // 10 MB
                // 保存文件
                try {
                    String fullPath = realityFileDepositPath + fileName;
                    try (FileOutputStream fileOutputStream = new FileOutputStream(new File(fullPath))) {
                        fileOutputStream.write(fileData);
                    }
                    System.out.println("公共文档已保存至：" + fullPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("公共文档上传失败，由于内部错误", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                // 返回文件虚拟路径
                return new ResponseEntity<>(virtualFileDepositPath.substring(0, virtualFileDepositPath.length() - 2) + fileName, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("公共文档大小超过10MB", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("公共文档格式不支持或为可执行文件，这是不允许的", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Resource> downloadFile(String fileName) {
        try {
            // 确定文件的存储路径
            Path filePath = Paths.get(realityFileDepositPath).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                // 确保文件名编码正确，避免乱码
                String encodedFileName = URLEncoder.encode(resource.getFilename(), StandardCharsets.UTF_8.toString());
                String contentDisposition = ContentDisposition.builder("attachment")
                        .filename(encodedFileName, StandardCharsets.UTF_8)
                        .build()
                        .toString();

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // 如果无法读取文件，返回错误响应
            return ResponseEntity.internalServerError().build();
        }
    }
}
