package zone.product.blogggcloudbe.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GoogleDriveFileDownLoadDTO {
    private String name;
    private byte[] data;
}
