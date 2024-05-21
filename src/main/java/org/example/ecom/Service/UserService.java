package org.example.ecom.Service;

import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findUserById(Long userId)throws UserException;
    public User findUserByUserName(String username) throws UserException;
    public Address addUserAddress(Address address,String username) throws UserException;
    public Address getUserAddress(Long id) throws UserException;
    public List<Address> getAllAddress(String username) throws UserException;
    public String deleteAddress(String username,Long id) throws UserException;
    public User updateUser(User user);
}
