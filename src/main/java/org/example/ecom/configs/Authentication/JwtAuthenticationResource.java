package org.example.ecom.configs.Authentication;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.ForUser.UserModel;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class JwtAuthenticationResource {
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private EmailService emailService;
    @PostMapping("/login")
    public JwtResponse authenticate(Authentication authentication){
//        return authentication;
        User user=userRepository.findByEmail(authentication.getName());
        return new JwtResponse(createToken(authentication),user.getUsername(),user.getRole());
    }
    @PostMapping("/login-status")
    public ResponseEntity<Boolean> check(Authentication authentication){
//        return authentication;
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/createNewUser")
    public String newUser(@RequestBody UserModel userModel) throws MessagingException {
        User u=userRepository.findByEmail(userModel.getEmail());
        if(u!=null){
            return "Already exist";
        }
        User user=new User(userModel.getFirstName(), userModel.getLastName(),passwordEncoder.encode(userModel.getPassword())
                , userModel.getEmail(), userModel.getMobile());
        user=userRepository.save(user);
        if (user!=null){
            cartService.createCart(user);
            emailService.sendEmailWithoutAttachment(userModel.getEmail(),"Dear "+userModel.getFirstName()+"\nYour account has been created \n Now you can log in and view our products" +
                    "\nThank you ","Welcome the store");
            return "sucess";
        }
        return "fail";

    }
    private String createToken(Authentication authentication) {
        var claims= JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*60*24))
                .subject(authentication.getName())
                .claim("scp",createScope(authentication))
                .build();
        var parameters= JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(parameters).getTokenValue();
//        return authentication.getName();
    }
    public String createScope(Authentication authentication){
        return authentication.getAuthorities().stream()
                .map(a->a.getAuthority())
                .collect(Collectors.joining(" "));
    }
    record JwtResponse(String token,String Username,String Role){}
}
