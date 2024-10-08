package com.gamezone.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRoleDTO {
    private Long userId;
    private String userName;
    private String role;
}