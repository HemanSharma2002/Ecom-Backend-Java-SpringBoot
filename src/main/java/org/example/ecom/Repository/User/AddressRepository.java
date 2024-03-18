package org.example.ecom.Repository.User;

import org.example.ecom.Entity.ForUser.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
}
