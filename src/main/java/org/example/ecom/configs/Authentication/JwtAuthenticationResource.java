package org.example.ecom.configs.Authentication;

import jakarta.mail.MessagingException;
import org.example.ecom.Entity.ApiResponse;
import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.ForUser.UserModel;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartService;
import org.example.ecom.Service.EmailService;
import org.example.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
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
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public JwtResponse authenticate(Authentication authentication){
        User user=userRepository.findByEmail(authentication.getName());
        return new JwtResponse(createToken(authentication),user.getUsername(),user.getRole(),user.getFirstName(),user.getLastName());
    }
    @PostMapping("/login-status")
    public ResponseEntity<Boolean> check(Authentication authentication){
//        return authentication;
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/createNewUser")
    public NewUserResponse newUser(@RequestBody UserModel userModel) throws MessagingException {
        User u=userRepository.findByEmail(userModel.getEmail());
        if(u!=null){
            if(u.getIsActive()){
                return  new NewUserResponse(false,null,"User exist, Please login ");
            }
            else {
                // resend otp
                String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
                u.setVerifyToken(otp);
                u.setVerifyTokenExp(LocalDateTime.now().plusHours(2));
                userRepository.save(u);
                emailService.sendEmailWithoutAttachment(userModel.getEmail(),"Your verification otp - "+otp,"Verification code");
                return  new NewUserResponse(false,u.getId(),"User exist and is not verified. Otp send please verify ");
            }
        }
        User user=new User(userModel.getFirstName(), userModel.getLastName(),passwordEncoder.encode(userModel.getPassword())
                , userModel.getEmail(), userModel.getMobile());
        //Otp generation
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        user.setVerifyToken(otp);
        user.setVerifyTokenExp(LocalDateTime.now().plusHours(2));

        user=userRepository.save(user);
        if (user!=null){
            cartService.createCart(user);
            emailService.sendEmailWithoutAttachment(userModel.getEmail(),"Your verification otp - "+otp,"Verification code");
            return  new NewUserResponse(true,user.getId(),"Signup Successful . Otp send verify yourself ");
        }
        return new NewUserResponse(false,null,"User exist, Please login ");

    }
    @PostMapping("/verify/{userId}")
    public ResponseEntity<ApiResponse> verifyUser(@PathVariable Long userId, @RequestParam String otp) throws MessagingException {
        System.out.println(otp);
        return new ResponseEntity<>(userService.verifyOtp(userId,otp),HttpStatus.OK);
    }
    @GetMapping("/resend-otp/{userId}")
    public ResponseEntity<ApiResponse> resendOtp(@PathVariable  Long userId) throws MessagingException {
        return new ResponseEntity<>(userService.generateNewOtp(userId),HttpStatus.OK);
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
    record JwtResponse(String token,String username,String Role,String firstName,String lastName){}
    record NewUserResponse(boolean sucess,Long userId,String message){}
}
