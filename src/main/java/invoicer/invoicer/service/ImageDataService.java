package invoicer.invoicer.service;

import invoicer.invoicer.model.ImageData;
import invoicer.invoicer.repository.ImageDataRepository;
import invoicer.invoicer.response.ImageDataResponse;
import invoicer.invoicer.response.ImageUploadResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ImageDataService {

    private final ImageDataRepository imageDataRepository;

    public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        ImageData imageData = new ImageData(fileName, file.getContentType(), file.getBytes());
        imageDataRepository.save(imageData);

        return ImageUploadResponse.builder()
                .message("File saved")
                .build();
    }
    public ImageDataResponse getFile(Long id) {
        ImageData file = imageDataRepository.findById(id).get();
        String fileDownloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/file/")
                .path(String.valueOf(id))
                .toUriString();
        return ImageDataResponse.builder()
                .name(file.getName())
                .url(fileDownloadUrl)
                .type(file.getType())
                .size(file.getImageData().length)
                .build();
    }

    public Stream<ImageData> getAllFiles() {
        return imageDataRepository.findAll().stream();
    }

}
