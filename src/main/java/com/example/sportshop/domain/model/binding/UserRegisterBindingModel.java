package com.example.sportshop.domain.model.binding;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserRegisterBindingModel {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;

    public UserRegisterBindingModel() {
    }

    @Length(min = 3,max = 15,message = "Username length must be between 3 and 15 characters")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty
    @Email(message = "Enter valid email.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min = 3,max = 20, message = "Password length must be between 3 and 20 characters")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
