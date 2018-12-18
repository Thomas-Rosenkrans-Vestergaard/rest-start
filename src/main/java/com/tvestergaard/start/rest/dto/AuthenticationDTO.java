package com.tvestergaard.start.rest.dto;

import com.tvestergaard.start.data.entities.User;

public class AuthenticationDTO
{

    public final String  token;
    public final UserDTO user;

    public AuthenticationDTO(String token, UserDTO userDTO)
    {
        this.token = token;
        this.user = userDTO;
    }

    public static AuthenticationDTO basic(String token, User user)
    {
        return new AuthenticationDTO(token, UserDTO.complete(user));
    }
}
