package zone.product.blogggcloudbe.Controller.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zone.product.blogggcloudbe.Controller.TestController;
import zone.product.blogggcloudbe.Model.DTO.GoogleDriveFileDownLoadDTO;
import zone.product.blogggcloudbe.Service.GoogleDriveService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class TestControllerImpl implements TestController {
    private final GoogleDriveService googleDriveService;
    @Autowired
    public TestControllerImpl(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @Override
    public ResponseEntity<Object> getAllDriveFile() throws IOException {
        return new ResponseEntity<>(googleDriveService.getAllFile(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> uploadSingleFile(MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(googleDriveService.uploadFile(multipartFile), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> downloadSingleFile(String fileId) throws IOException {
        GoogleDriveFileDownLoadDTO fileDownLoadDTO = googleDriveService.downloadFile(fileId);
        ByteArrayResource resource = new ByteArrayResource(fileDownLoadDTO.getData());
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileDownLoadDTO.getName());
        return ResponseEntity.ok()
                .headers(header)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
