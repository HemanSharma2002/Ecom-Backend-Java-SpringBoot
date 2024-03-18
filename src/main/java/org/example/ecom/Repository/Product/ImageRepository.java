package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ImageRepository extends JpaRepository<Images,Long> {
}
