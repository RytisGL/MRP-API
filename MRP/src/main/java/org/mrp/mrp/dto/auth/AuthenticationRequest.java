package org.mrp.mrp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message = "{validation.constraints.email.message}")
    @NotBlank(message = "{validation.constraints.email.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.email.name} {validation.constraints.size.message}")
    private String email;
    @NotBlank(message = "{validation.constraints.password.name} {validation.constraints.not_blank.message}")
    @Size(min = 5, max = 50, message = "{validation.constraints.password.name} {validation.constraints.size.message}")
    private String password;
}
