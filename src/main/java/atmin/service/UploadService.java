package atmin.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return null;
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isBlank()) {
                fileName = "file_" + System.currentTimeMillis();
            } else if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }

            Map<String, Object> uploadParams = Map.of(
                    "public_id", fileName
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult =
                    (Map<String, Object>) cloudinary.uploader()
                            .upload(file.getBytes(), uploadParams);

            return (String) uploadResult.get("secure_url");

        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }
}