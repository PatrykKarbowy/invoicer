package invoicer.invoicer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDataResponse {
    private String name;
    private String url;
    private String type;
    private long size;
}
