package invoicer.invoicer.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrReceiptResponse {
    private String receiptDate;
    private String receiptSum;
}
