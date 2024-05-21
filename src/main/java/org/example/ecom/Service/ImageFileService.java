package org.example.ecom.Service;

import lombok.AllArgsConstructor;
import org.example.ecom.Entity.ForProducts.ImageFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageFileService {
    public String saveImageToDatabase(MultipartFile image);
    public ImageFile getImage(String id);
}
