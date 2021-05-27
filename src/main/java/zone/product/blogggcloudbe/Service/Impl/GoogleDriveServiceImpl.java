package zone.product.blogggcloudbe.Service.Impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zone.product.blogggcloudbe.Model.DTO.GoogleDriveFileDownLoadDTO;
import zone.product.blogggcloudbe.Service.GoogleDriveService;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService {
    private static final String APPLICATION_NAME = "Fire Base Blog";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "googlecloud/blog-pj-fbver-4c832202749c.json";

    private static Drive drive;

    @PostConstruct
    public void initialize() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public List<File> getAllFile() throws IOException {
        FileList result = drive.files().list()
                .setFields("nextPageToken, files(id, name)")
                .execute();
        return result.getFiles();
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(multipartFile.getOriginalFilename());
        java.io.File filePath = new java.io.File("C:\\Data/"+multipartFile.getOriginalFilename());
        multipartFile.transferTo(filePath);
        FileContent mediaContent = new FileContent(multipartFile.getContentType(), filePath);
        File file = drive.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        return file.getId();
    }

    @Override
    public GoogleDriveFileDownLoadDTO downloadFile(String id) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        drive.files().get(id)
                .executeMediaAndDownloadTo(outputStream);
        return GoogleDriveFileDownLoadDTO.builder()
                .name(getFileNameFromId(id))
                .data(outputStream.toByteArray())
                .build();
    }


    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = FireBaseStarter.class.getClassLoader().getResourceAsStream("googlecloud/client.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String getFileNameFromId(String id) throws IOException {
        FileList result = drive.files().list()
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> fileList = result.getFiles();
        for (File file:
             fileList) {
            if (file.getId().equals(id)) return file.getName();
        }
        return "error";
    }
}
