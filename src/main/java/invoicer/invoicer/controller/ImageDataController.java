package invoicer.invoicer.controller;

import invoicer.invoicer.response.ImageDataResponse;
import invoicer.invoicer.response.ImageUploadResponse;
import invoicer.invoicer.service.ImageDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/file")
@RequiredArgsConstructor
public class ImageDataController {

    private final ImageDataService imageDataService;

    @PostMapping("upload")
    public ImageUploadResponse uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        return imageDataService.uploadImage(file);
    }

    @GetMapping("{id}")
    public ImageDataResponse getFileById(@PathVariable Long id){
        return imageDataService.getFile(id);
    }
}
