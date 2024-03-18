package org.example.ecom.configs.Authentication;

import org.example.ecom.Entity.ForUser.User;
import org.example.ecom.Entity.ForUser.UserModel;
import org.example.ecom.Repository.User.UserRepository;
import org.example.ecom.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/login")
    public JwtResponse authenticate(Authentication authentication){
//        return authentication;
        return new JwtResponse(createToken(authentication));
    }

    @PostMapping("/createNewUser")
    public String newUser(@RequestBody UserModel userModel){
        User user=new User(userModel.getFirstName(), userModel.getLastName(),passwordEncoder.encode(userModel.getPassword())
                , userModel.getEmail(), userModel.getMobile());
        user=userRepository.save(user);
        if (user!=null){
            cartService.createCart(user);

            return "sucess";
        }
        return "fail";

    }
    private String createToken(Authentication authentication) {
        var claims= JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60*60*24))
                .subject(authentication.getName())
                .claim("scope",createScope(authentication))
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

    record JwtResponse(String token){}
}
