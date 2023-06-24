package invoicer.invoicer.controller;

import invoicer.invoicer.response.OcrResponse;
import invoicer.invoicer.service.OcrService;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;

    @PostMapping("recognize")
    public OcrResponse recognize (@RequestParam("file-id") Long id) throws TesseractException, IOException {
        return ocrService.recognize(id);
    }
}
