package org.example.ecom.Repository.User;

import org.example.ecom.Entity.ForUser.PaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentImformationRepository extends JpaRepository<PaymentInformation,Long> {
}
