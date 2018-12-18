package com.tvestergaard.start.rest.dto;

import com.tvestergaard.start.data.entities.User;

import java.time.LocalDateTime;

public class UserDTO
{
    private final Integer       id;
    private final String        name;
    private final String        email;
    private final LocalDateTime createdAt;

    private UserDTO(Integer id, String name, String email, LocalDateTime createdAt)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static UserDTO complete(User user)
    {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
