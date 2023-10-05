package com.example.amazon.Services;

import com.example.amazon.Config.EmailEvent;
import com.example.amazon.DTO.UserDTO;
import com.example.amazon.Entities.AmazonResponseCode;
import com.example.amazon.Entities.AmazonResponseEntity;
import com.example.amazon.Entities.Cart;
import com.example.amazon.Entities.User.Role;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Repositories.UserRepo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private UserRepo userRepo;
    private CartService cartService;
    private CartProductService cartProductService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private ApplicationEventPublisher eventPublisher;

    public UserService(UserRepo userRepo, CartService cartService, CartProductService cartProductService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, ApplicationEventPublisher eventPublisher) {
        this.userRepo = userRepo;
        this.cartService = cartService;
        this.cartProductService = cartProductService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AmazonResponseEntity<?> createUser(User user){
        if(user.getUsername() == null || user.getPassword() == null || user.getEmail() == null
                || user.getSsn() == null || user.getFirstName() == null
                || user.getLastName() == null || user.getNumber() == null){
            return new AmazonResponseEntity<>(AmazonResponseCode.USER_FIELD_IS_NULL);
        }
        Cart cart = new Cart();                                             //create cart for user
        cartService.createCart(cart);                                       //save cart
        user.setCart(cart);                                                 //set cart for user
        user.setRole(Role.USER);                                            //set role for user
        user.setPassword(passwordEncoder.encode(user.getPassword()));       //encode password
        userRepo.save(user);                                                //save user
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, jwtService.generateToken(user));
    }

    @Transactional
    public AmazonResponseEntity<?> deleteUserById(long id){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent() && !user.get().getDeleted()){
            user.get().setDeleted(true);                                            //set isDeleted to true
            user.get().setEmail(null);                                              //delete email
            user.get().setSsn(null);                                                //delete ssn
            cartProductService.deleteCartProductsByCart(user.get().getCart());      //delete all products from this cart
            Cart cart = user.get().getCart();
            user.get().setCart(null);                                               //set cart null
            cartService.deleteCart(cart.getId());                                   //delete cart
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    @Transactional
    public AmazonResponseEntity<?> deleteUserByUsername(String username){
        Optional<User> user = userRepo.findByUsername(username);
            user.get().setDeleted(true);                                            //set isDeleted to true
            user.get().setEmail(null);                                              //delete email
            user.get().setSsn(null);                                                //delete ssn
            cartProductService.deleteCartProductsByCart(user.get().getCart());      //delete all products from this cart
            Cart cart = user.get().getCart();
            user.get().setCart(null);                                               //set cart null
            cartService.deleteCart(cart.getId());                                   //delete cart
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS);
    }

    public AmazonResponseEntity<?> getById(long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent() && !user.get().getDeleted()){
            user.get().setPassword(null);
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, user);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    public Optional<User> getByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public AmazonResponseEntity<?> getAll(){
         List<User> users = userRepo.findAll();
         if(users.isEmpty()){
             return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
         }
         users.forEach(user -> user.setPassword(null));
        return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, users);
    }

    public AmazonResponseEntity<?> updateUser(UserDTO userDto, String username){
        Optional<User> user1 = userRepo.findByUsername(username);      //get user
        if(user1.isPresent() && !user1.get().getDeleted()){
            if(userDto.getFirstName() == null){                         //check which attributes are updated
                userDto.setFirstName(user1.get().getFirstName());
            }
            if(userDto.getLastName() == null){
                userDto.setLastName(user1.get().getLastName());
            }
            if(userDto.getNumber() == null){
                userDto.setNumber(user1.get().getNumber());
            }
            user1.get().setFirstName(userDto.getFirstName());           //set user values
            user1.get().setLastName(userDto.getLastName());
            user1.get().setNumber(userDto.getNumber());
            userRepo.save(user1.get());
            user1.get().setPassword(null);
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, user1);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    public AmazonResponseEntity<?> updateUserPassword(User user, String username){
        Optional<User> user1 = userRepo.findByUsername(username);                      //get user
        if(user1.isPresent() && !user1.get().getDeleted()){
            user1.get().setPassword(passwordEncoder.encode(user.getPassword()));       //encode password
            userRepo.save(user1.get());                                                //set user credentials
            user1.get().setPassword(null);
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, user1);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    public AmazonResponseEntity<?> authenticateUser(User user) {
        Optional<User> user1 = userRepo.findByUsername(user.getUsername());
        if(user1.isPresent() && !user1.get().getDeleted()){
            try{
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            }catch (Exception e){
                return new AmazonResponseEntity<>(AmazonResponseCode.LOGIN_FAILED);
            }
            return new AmazonResponseEntity<>(AmazonResponseCode.LOGIN_SUCCESS, jwtService.generateToken(user1.get()));
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.LOGIN_FAILED);
    }

    public AmazonResponseEntity<?> getLoggedInUser(String username){
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()){
            user.get().setPassword(null);
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, user);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    public AmazonResponseEntity<?> generateOtp(String username){
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()){
            int random = new Random().nextInt(9000) + 1000;         //generate OTP
            String otp = Integer.toString(random);
            user.get().setOtp(otp);
            EmailEvent event = new EmailEvent(this, user.get().getEmail(), otp, "OTP CODE", user.get().getUsername());
            eventPublisher.publishEvent(event);                           //send OTP
            userRepo.save(user.get());
            return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }

    public AmazonResponseEntity<?> checkOtp(String otp, String username){
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()){
            if(user.get().getOtp().equals(otp)){
                user.get().setOtp(null);
                userRepo.save(user.get());
                return new AmazonResponseEntity<>(AmazonResponseCode.SUCCESS, jwtService.generateToken(user.get()));
            }
            user.get().setOtp(null);
            userRepo.save(user.get());
            return new AmazonResponseEntity<>(AmazonResponseCode.WRONG_OTP);
        }
        return new AmazonResponseEntity<>(AmazonResponseCode.USER_NOT_FOUND);
    }
}
