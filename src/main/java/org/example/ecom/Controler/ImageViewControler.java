package org.example.ecom.Controler;

import lombok.AllArgsConstructor;
import org.example.ecom.Entity.ForProducts.ImageFile;
import org.example.ecom.Service.ImageFileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/imageStore")
public class ImageViewControler {
    private ImageFileService imageFileService;
    @GetMapping("/view/{fileId}")
    public ResponseEntity<?> viewFile(@PathVariable("fileId")String id){
        ImageFile image=imageFileService.getImage(id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image.getData());
    }
}
