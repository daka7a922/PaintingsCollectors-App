package com.paintingscollectors.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters")
    @NotNull
    private String username;

    @Email
    @NotNull
    private String email;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    @NotNull
    private String password;

    @NotNull
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password must match")
    public boolean isPasswordMatching() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

}
