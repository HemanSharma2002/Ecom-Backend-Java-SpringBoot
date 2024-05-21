package org.example.ecom.Service.Impliment;

import lombok.AllArgsConstructor;
import org.example.ecom.Entity.ForProducts.ImageFile;
import org.example.ecom.Repository.Product.ImageFileRepository;
import org.example.ecom.Service.ImageFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@AllArgsConstructor
public class ImageFileServiceImp implements ImageFileService {
    private ImageFileRepository imageFileRepository;
    @Override
    public String saveImageToDatabase(MultipartFile image) {
        String filename= StringUtils.cleanPath(image.getOriginalFilename());
        try{
            if(filename.contains("..")){
                throw  new RuntimeException("File Name Contain invalid data sequence");
            }
            ImageFile NewImage= ImageFile.builder()
                    .filename(filename)
                    .filetype(image.getContentType())
                    .data(image.getBytes()).build();
            NewImage=imageFileRepository.save(NewImage);
            String downloadURL= ServletUriComponentsBuilder.fromCurrentContextPath().path("/imageStore/view/").path(NewImage.getId()).toUriString();
            return downloadURL;
        }
        catch(Exception e){
            throw new RuntimeException("Could not save Image "+filename);
        }
    }

    @Override
    public ImageFile getImage(String id) {
        return imageFileRepository.findById(id).orElseThrow(()->new RuntimeException("File not found id:"+id));
    }
}
