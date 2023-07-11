package com.internship.blog.payloads;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;

    @NotEmpty
    @Size(min=4,message = "Username must be min of 4 characters")
    private String name;

    @Email(message = "Email Address is not valid")
    private String email;

    @NotEmpty(message = "Password must not be blank")
    @Size(min =6, max=12, message = "Password should be within range 6 to 12 characters")
//    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,12}$", message = "Check Password Constraints")
    private String password;

    @NotEmpty(message = "About must not be blank")
    private String about;

    private Set<RoleDto> roles = new HashSet<>();
}
