package org.example.ecom.Service.Impliment;

import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.User.AddressRepository;
import org.example.ecom.Repository.User.PaymentImformationRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private PaymentImformationRepository paymentImformationRepository;

    public UserServiceImplement(UserRepository userRepository, AddressRepository addressRepository, PaymentImformationRepository paymentImformationRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentImformationRepository = paymentImformationRepository;
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("User not found with id "+userId);
    }

    public User findUserByUserName(String email) throws UserException {
        System.out.println("HEman");
        User user=userRepository.findByEmail(email);
        if(user!=null){
            return user;
        }
        throw new UserException("User not found with id "+email);
    }

    @Override
    public Address addUserAddress(Address address,String username) throws UserException {
        User user=findUserByUserName(username);
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getAllAddress(String username) throws UserException {
        User user=findUserByUserName(username);
        return user.getAddress();
    }

    @Override
    public String deleteAddress(String username, Long id) throws UserException {
        Optional<Address> address=addressRepository.findById(id);
        if(address.isPresent()&&address.get().getUser().getUsername().equals(username)){
            addressRepository.deleteById(id);
            return "Sucess";
        }
        return "Failed";
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Address getUserAddress(Long id) throws UserException {
        Optional<Address> address=addressRepository.findById(id);
        if(address.isPresent()){
            return address.get();
        }
        return null ;
    }
}
