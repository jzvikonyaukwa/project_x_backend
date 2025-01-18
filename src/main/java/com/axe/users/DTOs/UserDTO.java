package com.axe.users.DTOs;

import com.axe.common.enums.UserGroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
 
    private String username;
 
    private String fullname;
 
    private String email;
 
    private UserGroup groups;
  
    private Boolean isActive;
}
