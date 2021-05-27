package zone.product.blogggcloudbe.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("test/")
public interface TestController {
    @GetMapping("drive/all")
    @Operation(description = "Get All File List From GG Drive", summary = "Get All File List From GG Drive" )
    public ResponseEntity<Object> getAllDriveFile() throws IOException;

    @PostMapping(value = "drive/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Upload single File to GG Drive", summary ="Upload single File to GG Drive")
    public  ResponseEntity<Object> uploadSingleFile(@RequestParam(name = "file") @Parameter(description = "Upload File")MultipartFile multipartFile) throws IOException;

    @GetMapping(value = "drive/download")
    @Operation(description = "Download a File from GG Drive Using file ID", summary = "Download a File from GG Drive Using file ID")
    public ResponseEntity<Object> downloadSingleFile(@RequestParam(name = "id") @Parameter(description = "File ID", example = "195BklB3mjJSOGvQ-zzkmK6IzCJD9Cx1P") String fileId) throws IOException;
}
