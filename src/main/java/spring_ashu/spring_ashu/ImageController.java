package spring_ashu.spring_ashu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // ✅ POST /api/images/upload
    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String id = imageService.uploadImage(file);
            return ResponseEntity.ok(Map.of(
                    "message", "Upload successful!",
                    "id", id
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ GET /api/images/{id}
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        try {
            byte[] imageData = imageService.getImage(id);
            String contentType = imageService.getContentType(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ DELETE /api/images/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable String id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Image deleted successfully!",
                    "id", id
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ GET /api/images
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }
}