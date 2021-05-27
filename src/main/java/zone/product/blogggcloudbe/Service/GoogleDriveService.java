package zone.product.blogggcloudbe.Service;

import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;
import zone.product.blogggcloudbe.Model.DTO.GoogleDriveFileDownLoadDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface GoogleDriveService {
    List<File> getAllFile() throws IOException;
    String uploadFile(MultipartFile multipartFile) throws IOException;
    GoogleDriveFileDownLoadDTO downloadFile(String id) throws IOException;
}
