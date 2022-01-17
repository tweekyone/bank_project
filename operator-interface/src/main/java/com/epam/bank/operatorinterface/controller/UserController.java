package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.CreateUserRequest;
import com.epam.bank.operatorinterface.controller.dto.response.UserResponse;
import com.epam.bank.operatorinterface.controller.mapper.UserMapper;
import com.epam.bank.operatorinterface.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper responseMapper;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(
            responseMapper.map(userService.create(
                request.getName(),
                request.getSurname(),
                request.getPhoneNumber(),
                request.getUsername(),
                request.getEmail(),
                request.getRawPassword()
            )),
            HttpStatus.CREATED
        );
    }

    @PostMapping("/client")
    public ResponseEntity<UserResponse> createClient(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(
            responseMapper.map(userService.createClient(
                request.getName(),
                request.getSurname(),
                request.getPhoneNumber(),
                request.getUsername(),
                request.getEmail(),
                request.getRawPassword()
            )),
            HttpStatus.CREATED
        );
    }
}
