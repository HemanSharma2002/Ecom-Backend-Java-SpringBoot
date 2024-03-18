package org.example.ecom.Service;

import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.UserException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findUserById(Long userId)throws UserException;
    public User findUserByUserName(String username) throws UserException;
}
