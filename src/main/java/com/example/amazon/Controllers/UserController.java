package com.example.amazon.Controllers;

import com.example.amazon.DTO.UserDTO;
import com.example.amazon.Entities.AmazonResponseCode;
import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Services.JwtService;
import com.example.amazon.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private JwtService jwtService;
    public UserController(UserService userService, JwtService jwtService){
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/{id}")
    public AmazonResponseEntity<?> getUser(@PathVariable long id){
        return userService.getById(id);
    }

    @GetMapping("/info")
    public AmazonResponseEntity<?> getLoggedInUser(HttpServletRequest request){
        return userService.getLoggedInUser(jwtService.extractUsernameFromRequestHeader(request));
    }

    @GetMapping("")
    public AmazonResponseEntity<?> getAllUsers(){
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public AmazonResponseEntity<?> deleteUser(@PathVariable long id){
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/delete")
    public AmazonResponseEntity<?> deleteUser(HttpServletRequest request){
        return userService.deleteUserByUsername(jwtService.extractUsernameFromRequestHeader(request));
    }

    @PostMapping("/register")
    public AmazonResponseEntity<?> registerUser(@RequestBody User user){
        try {
            return userService.createUser(user);
        }catch (DataIntegrityViolationException dive){
            return new AmazonResponseEntity<>(AmazonResponseCode.USERNAME_EMAIL_SSN_ALREADY_EXIST);
        }
    }

    @PostMapping("/authenticate")
    public AmazonResponseEntity<?> authenticateUser(@RequestBody User user){
        return userService.authenticateUser(user);
    }

    @PutMapping("")
    public AmazonResponseEntity<?> updateUser(@RequestBody UserDTO userDto, HttpServletRequest request){
        return userService.updateUser(userDto, jwtService.extractUsernameFromRequestHeader(request));
    }

    @PutMapping("/password")
    public AmazonResponseEntity<?> updateUserPassword(@RequestBody User user, HttpServletRequest request){
        return userService.updateUserPassword(user, jwtService.extractUsernameFromRequestHeader(request));
    }

    @PostMapping("/otp/generate")
    public AmazonResponseEntity<?> generateOtp(@RequestBody User user){
        if(user.getUsername() == null){
            return new AmazonResponseEntity<>(AmazonResponseCode.USER_FIELD_IS_NULL);
        }
        return userService.generateOtp(user.getUsername());
    }

    @PostMapping("/otp/validate")
    public AmazonResponseEntity<?> checkOtp(@RequestBody User user){
        if(user.getUsername() == null || user.getOtp() == null){
            return new AmazonResponseEntity<>(AmazonResponseCode.USER_FIELD_IS_NULL);
        }
        return userService.checkOtp(user.getOtp(), user.getUsername());
    }
}
