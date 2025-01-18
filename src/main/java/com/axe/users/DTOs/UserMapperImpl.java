package com.axe.users.DTOs;

import com.axe.users.User; 
 
public class UserMapperImpl  {
 
    public UserDTO fromNotification(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setUsername( user.getUsername() );
        userDTO.setFullname( user.getFullname() );
        userDTO.setEmail( user.getEmail() );
        // userDTO.setGroups( user.getGroups() );
        userDTO.setIsActive( user.getIsActive() );

        return userDTO;
    }
 
    public User fromUserDTO(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.username( userDTO.getUsername() );
        user.fullname( userDTO.getFullname() );
        user.email( userDTO.getEmail() );
        // user.groups( userDTO.getGroups() );
        user.isActive( userDTO.getIsActive() );

        return user.build();
    }
}
