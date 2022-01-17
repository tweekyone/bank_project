package com.epam.bank.operatorinterface.service.dto;

import com.epam.bank.operatorinterface.util.validator.EmailIsUnique;
import com.epam.bank.operatorinterface.util.validator.UsernameIsUnique;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {
    @NotNull @NotBlank @Size(min = 2, max = 255)
    private String name;

    @NotNull @NotBlank @Size(min = 2, max = 255)
    private String surname;

    @NotNull @NotBlank @Pattern(regexp = "[+()\\-0-9]+") @Size(min = 5, max = 20)
    private String phone;

    @NotNull @NotBlank @Size(min = 4, max = 255) @UsernameIsUnique
    private String username;

    @NotNull @NotBlank @Email @EmailIsUnique
    private String email;

    @NotNull @NotBlank @Size(min = 8, max = 255)
    private String rawPassword;
}
