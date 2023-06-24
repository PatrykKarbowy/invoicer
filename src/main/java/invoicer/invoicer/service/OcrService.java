package invoicer.invoicer.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import invoicer.invoicer.model.ImageData;
import invoicer.invoicer.repository.ImageDataRepository;
import invoicer.invoicer.response.OcrResponse;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import static invoicer.invoicer.util.ImageProcessor.*;
import static invoicer.invoicer.util.OcrDataProcessor.contentSeparator;

@Service
@AllArgsConstructor
public class OcrService {

    private final ImageDataRepository imageDataRepository;
    public OcrResponse recognize(Long id) throws IOException, TesseractException {
        ImageData image = imageDataRepository.findById(id).orElseThrow();
        byte[] imageData = image.getImageData();
        ByteArrayInputStream imageFile = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        BufferedImage bufferedProceedImage = processForOCR(bufferedImage,60);
        ITesseract instance = new Tesseract();
        instance.setOcrEngineMode(1);
        instance.setPageSegMode(4);
        instance.setDatapath("C:\\Users\\Togo\\Desktop\\Coding\\Java\\Spring\\invoicer\\src\\main\\resources\\static");
        instance.setLanguage("pol");
        String content = instance.doOCR(bufferedProceedImage);
        ArrayList<String> separatedContent = contentSeparator(content);

        return OcrResponse.builder()
        .content(separatedContent)
        .build();
    }
}
