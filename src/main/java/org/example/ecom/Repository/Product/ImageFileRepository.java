package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile,String> {
}
