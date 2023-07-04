package rebbit.com.example.rebbit.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rebbit.com.example.rebbit.dto.LoginDTO;
import rebbit.com.example.rebbit.dto.UserDTO;
import rebbit.com.example.rebbit.model.User;
import rebbit.com.example.rebbit.security.TokenUtils;
import org.springframework.security.core.userdetails.UserDetails;
import rebbit.com.example.rebbit.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    private final UserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, TokenUtils tokenUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
            return ResponseEntity.ok(tokenUtils.generateToken(userDetails));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        User user = userService.findByUsername(userDTO.getUsername());
        if (user != null) {
            return ResponseEntity.badRequest().body("Username taken!");
        }
        String password = passwordEncoder.encode(userDTO.getPassword());
        User userNew = new User(userDTO.getUsername(), password, userDTO.getEmail(), LocalDate.now(), userDTO.getDisplayName());
        return ResponseEntity.ok(userService.register(userNew));
    }
}
