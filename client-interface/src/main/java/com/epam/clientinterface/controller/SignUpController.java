package com.epam.clientinterface.controller;

import com.epam.clientinterface.dto.UserDto;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import com.epam.clientinterface.service.AuthService;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/")
public class SignUpController {

    private final AuthService authService;

    @Autowired
    public SignUpController(AuthService authService) {
        this.authService = authService;
    }

    // Test controller
    @GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/user/registration")
    public ResponseEntity<User> registerUserAccount(@RequestBody @Valid UserDto userDto,
                                                    HttpServletRequest request) throws UserAlreadyExistException {
        User registered = authService.signUp(
            userDto.getName(), userDto.getSurname(),
            userDto.getPhoneNumber(), userDto.getUsername(),
            userDto.getEmail(), userDto.getPassword());
        if (registered == null) {
            throw new UserAlreadyExistException("This user already registered: " + userDto.getUsername()
                + ", with email: " + userDto.getEmail());
        }
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

}
