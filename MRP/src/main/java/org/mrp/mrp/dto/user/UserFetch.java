package org.mrp.mrp.dto.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.mrp.mrp.enums.Role;

@Data
public class UserFetch {
    private long id;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
}
