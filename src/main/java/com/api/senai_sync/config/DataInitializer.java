package com.api.senai_sync.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.api.senai_sync.entity.Role;
import com.api.senai_sync.entity.User;
import com.api.senai_sync.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("master").isEmpty()) {
            User masterUser = new User();
            masterUser.setName("Master User");
            masterUser.setUsername("master");
            masterUser.setPassword(passwordEncoder.encode("master"));
            masterUser.setRole(Role.MASTER);

            userRepository.save(masterUser);
            System.out.println("Usuário MASTER criado com sucesso.");
        } else {
            System.out.println("Usuário MASTER já existe.");
        }
    }
}
