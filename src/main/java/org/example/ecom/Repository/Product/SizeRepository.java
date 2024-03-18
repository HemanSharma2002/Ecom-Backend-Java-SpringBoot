package org.example.ecom.Repository.Product;

import org.example.ecom.Entity.ForProducts.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size,Long> {
}
