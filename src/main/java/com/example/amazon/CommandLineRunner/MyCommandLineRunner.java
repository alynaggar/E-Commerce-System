package com.example.amazon.CommandLineRunner;

import com.example.amazon.Entities.User.Role;
import com.example.amazon.Entities.User.User;
import com.example.amazon.Repositories.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public MyCommandLineRunner(UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String...args) throws Exception {
        Optional<User> user = userRepo.findByUsername("admin");
        if(!user.isPresent()){
            User admin = new User("admin", passwordEncoder.encode("admin"), Role.ADMIN, "admin", "admin");
            userRepo.save(admin);
        }
    }
}

