package spring_ashu.spring_ashu;


import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations gridFsOperations;

    // ✅ UPLOAD
    public String uploadImage(MultipartFile file) throws IOException {
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType()
        );
        return id.toString();
    }

    // ✅ GET IMAGE BYTES
    public byte[] getImage(String id) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(id)))
        );
        if (file == null) throw new RuntimeException("Image not found!");

        try (InputStream is = gridFsOperations.getResource(file).getInputStream()) {
            return is.readAllBytes();
        }
    }

    // ✅ GET CONTENT TYPE
    public String getContentType(String id) {
        GridFSFile file = gridFsTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(id)))
        );
        if (file == null) throw new RuntimeException("Image not found!");
        return file.getMetadata() != null
                ? file.getMetadata().getString("_contentType")
                : "image/jpeg";
    }


    // ✅ DELETE image by ID
    public void deleteImage(String id) {
        gridFsTemplate.delete(
                new Query(Criteria.where("_id").is(new ObjectId(id)))
        );
    }

    // ✅ LIST all images
    public List<Map<String, String>> getAllImages() {
        List<Map<String, String>> images = new ArrayList<>();
        gridFsTemplate.find(new Query()).forEach(file -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", file.getObjectId().toString());
            map.put("filename", file.getFilename());
            map.put("contentType", file.getMetadata() != null
                    ? file.getMetadata().getString("_contentType")
                    : "unknown");
            images.add(map);
        });
        return images;
    }

}