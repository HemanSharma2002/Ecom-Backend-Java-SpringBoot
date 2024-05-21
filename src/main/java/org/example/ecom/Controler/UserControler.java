package org.example.ecom.Controler;

import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.example.ecom.Entity.ForUser.Address;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Exceptions.UserException;
import org.example.ecom.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserControler {
    private UserService userService;
    @PostMapping("/address")
    public Address addUserAddress( Authentication authentication,@RequestBody Address address) throws UserException {
        Address newAddress = userService.addUserAddress(address, authentication.getName());
        putActiveAddress(authentication,newAddress.getId());
        return newAddress;
    }
    @GetMapping("/address")
    public List<Address> getAllAddress(Authentication authentication) throws UserException {
        return userService.getAllAddress(authentication.getName());
    }
    @DeleteMapping("/address/{id}")
    public String removeAddress(Authentication authentication,@PathVariable Long id) throws UserException {
        return userService.deleteAddress(authentication.getName(),id);
    }
    @PutMapping("/activeAddress/{addressId}")
    public Address putActiveAddress(Authentication authentication,@PathVariable Long addressId) throws UserException {
        User user=userService.findUserByUserName(authentication.getName());
        Address address=userService.getUserAddress(addressId);
        user.setActiveAddress(address);
        return userService.updateUser(user).getActiveAddress();
    }
    @GetMapping("/activeAddress")
    public Address getUserActiveAddress(Authentication authentication) throws UserException {
        return userService.findUserByUserName(authentication.getName()).getActiveAddress();
    }
    @GetMapping("/username")
    public UserInfo getUsername(Authentication authentication) throws UserException {
        User user=userService.findUserByUserName(authentication.getName());
        System.out.println(authentication.getAuthorities());
        return new UserInfo(user.getUsername(), user.getRole());
    }
}
record UserInfo(String Username,String Role){

}