package com.epam.clientinterface.controller;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.dto.UserDto;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.service.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SignUpController {

    private final AuthService authService;

    @Autowired
    public SignUpController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user/registration")
    public String testRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/user/registration")
    public ResponseEntity<User> registerUserAccount(@RequestBody @Valid UserDto userDto)
        throws UserAlreadyExistException {

        User registered = authService.signUp(
            userDto.getName(), userDto.getSurname(),
            userDto.getPhoneNumber(), userDto.getUsername(),
            userDto.getEmail(), userDto.getPassword());

        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

}
