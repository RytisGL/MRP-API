package org.mrp.mrp.converters;

import org.mrp.mrp.dto.user.UserFetch;
import org.mrp.mrp.entities.User;

public abstract class UserConverter {
    private UserConverter(){}

    public static UserFetch userToUserDTO(User user) {
        UserFetch userFetch = new UserFetch();
        userFetch.setId(user.getId());
        userFetch.setEmail(user.getEmail());
        userFetch.setRole(user.getRole());
        return userFetch;
    }
}
