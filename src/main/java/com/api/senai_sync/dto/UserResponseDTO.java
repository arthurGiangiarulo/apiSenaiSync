package com.api.senai_sync.dto;

import com.api.senai_sync.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String username;
    private Role role;
}

