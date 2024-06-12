package org.mrp.mrp.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "{validation.constraints.email.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.email.name} {validation.constraints.size.message}")
    private String email;
    @NotBlank(message = "{validation.constraints.password.name}  {validation.constraints.not_blank.message}")
    @Size(min = 8, max = 20, message = "{validation.constraints.password.name} {validation.constraints.size.message}")
    private String password;
}
