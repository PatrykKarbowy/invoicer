package invoicer.invoicer.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class ImageData {

    @Id
    @SequenceGenerator(
            name = "file_sequence",
            sequenceName = "file_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_sequence"
    )
    private Long id;
    private String name;
    private String type;

    @Lob
    private byte[] imageData;

    public ImageData(String name, String type, byte[] imageData){
        this.name = name;
        this.type = type;
        this.imageData = imageData;
    }
}
