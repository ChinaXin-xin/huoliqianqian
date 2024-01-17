package xin.admin.controller.commonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xin.admin.domain.base64.UploadRequest;
import xin.admin.service.commonality.UpdateOrDownloadService;

@RestController
public class UpdateOrDownloadController {

    @Autowired
    UpdateOrDownloadService updateOrDownloadService;

    @PostMapping("/api/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        return updateOrDownloadService.handleFileUpload(file);
    }

    @PostMapping("/api/uploadBase64")
    public ResponseEntity<String> handleFileUploadBase64(@RequestBody UploadRequest uploadRequest) {
        return updateOrDownloadService.handleFileUploadBase64(uploadRequest.getBase64File(), uploadRequest.getFileName());
    }

    @GetMapping("/api/upload/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        return updateOrDownloadService.downloadFile(fileName);
    }
}
