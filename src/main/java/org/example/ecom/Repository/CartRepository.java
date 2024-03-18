package org.example.ecom.Repository;

import org.example.ecom.Entity.Cart;
import org.example.ecom.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

}
