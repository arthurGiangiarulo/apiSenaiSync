package com.api.senai_sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.senai_sync.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

