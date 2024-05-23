package org.example.ecom.Service.Impliment;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.ApiResponse;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Repository.User.AddressRepository;
import org.example.ecom.Repository.User.PaymentImformationRepository;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.EmailService;
import org.example.ecom.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImplement implements UserService {
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private PaymentImformationRepository paymentImformationRepository;
    private EmailService emailService;

    public UserServiceImplement(EmailService emailService,UserRepository userRepository, AddressRepository addressRepository, PaymentImformationRepository paymentImformationRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.paymentImformationRepository = paymentImformationRepository;
        this.emailService=emailService;
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

    @Override
    public ApiResponse verifyOtp(Long userId, String otp) throws MessagingException {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            if(user.get().getVerifyToken().equals(otp)){
                if(user.get().getVerifyTokenExp().isAfter(LocalDateTime.now())){
                    user.get().setIsActive(true);
                    userRepository.save(user.get());
                    return new ApiResponse(true,"User verified . Login to the store",null);
                }
                String newOtp= new DecimalFormat("000000").format(new Random().nextInt(999999));
                user.get().setVerifyToken(newOtp);
                user.get().setVerifyTokenExp(LocalDateTime.now().plusHours(2));
                userRepository.save(user.get());
                emailService.sendEmailWithoutAttachment(user.get().getEmail(),"Your verification otp - "+newOtp,"Verification code");
                return new ApiResponse(false,"Otp is expired . New otp send",null);
            }
            return new ApiResponse(false,"Otp doesn't match",null);
        }
        return  new ApiResponse(false,"Failed verify generate otp",null);
    }

    @Override
    public ApiResponse generateNewOtp(Long userId) throws MessagingException {
        Optional<User> user=userRepository.findById(userId);
        if(user.isPresent()){
            String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
            user.get().setVerifyToken(otp);
            user.get().setVerifyTokenExp(LocalDateTime.now().plusHours(2));
            userRepository.save(user.get());
            emailService.sendEmailWithoutAttachment(user.get().getEmail(),"Your verification otp - "+otp,"Verification code");
            return new ApiResponse(true,"Otp is send to email",null);
        }
        return new ApiResponse(false,"Failed to generate otp",null);
    }

}
