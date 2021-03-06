package com.spd.trello.controller;

import com.spd.trello.domain.resources.User;
import com.spd.trello.security.AuthenticationRequest;
import com.spd.trello.security.JwtTokenProvider;
import com.spd.trello.security.RegistrationRequest;
import com.spd.trello.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private UserService userService;
    private JwtTokenProvider tokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, PasswordEncoder encoder, UserService userService, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authentication(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userService.findUserByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn`t exist"));
            String token = tokenProvider.createToken(request.getEmail(), request.getPassword());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", request.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest request){
        if(userService.findUserByEmail(request.getEmail()).isPresent()){
            return new ResponseEntity<>("Email already exist!", HttpStatus.BAD_REQUEST);
        }
        request.setPassword(encoder.encode(request.getPassword()));
        User save = userService.save(request);
        String token = tokenProvider.createToken(request.getEmail(), request.getPassword());
        Map<Object, Object> response = new HashMap<>();
        response.put("firstname", save.getFirstName());
        response.put("lastname", save.getLastName());
        response.put("email", save.getEmail());
        response.put("password", save.getPassword());
        response.put("token", token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setClearAuthentication(true);
        logoutHandler.logout(request, response, null);
    }
}
