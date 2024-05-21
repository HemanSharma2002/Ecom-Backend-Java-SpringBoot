package org.example.ecom.Repository;

import org.example.ecom.Entity.Enum.OrderStatus;
import org.example.ecom.Entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    Page<Orders> findByOrderStatus(OrderStatus orderStatus, Pageable pageable);
    Page<Orders> findById(Long id, Pageable pageable);
}
