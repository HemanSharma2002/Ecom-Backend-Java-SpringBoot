package org.example.ecom.Repository;

import org.example.ecom.Entity.OrderItem;
import org.example.ecom.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
