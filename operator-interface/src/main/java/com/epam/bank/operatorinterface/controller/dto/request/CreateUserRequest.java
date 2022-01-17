package com.epam.bank.operatorinterface.controller.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull @NotBlank
    private String name;

    @NotNull @NotBlank
    private String surname;

    @NotNull @NotBlank
    private String phoneNumber;

    @NotNull @NotBlank
    private String username;

    @NotNull @NotBlank @Email
    private String email;

    @NotNull @NotBlank
    private String rawPassword;
}
