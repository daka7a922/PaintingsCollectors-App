package com.paintingscollectors.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotNull
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    private String username;

    @NotNull
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    private String password;
}
