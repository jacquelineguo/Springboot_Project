package com.neu.easypay.controller;

import com.neu.easypay.exception.DuplicateException;
import com.neu.easypay.model.ExceptionOutput;
import com.neu.easypay.model.SigninInput;
import com.neu.easypay.model.SigninOutput;
import com.neu.easypay.model.SignupInput;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.impl.UserAccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserAccountController {

    private final @NonNull UserAccountService usersAccountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupInput signupInput) {
        final UserAccount userAccount = UserAccount.builder()
                .uuid(UUID.randomUUID().toString())
                .username(signupInput.getUsername())
                .email(signupInput.getEmail())
                .password(signupInput.getPassword())
                .name(signupInput.getName())
                .build();

        final UserAccount createdUserAccount;

        try {
            createdUserAccount = usersAccountService.create(userAccount);
        } catch (DuplicateException exception) {
            final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                    .message(exception.getMessage())
                    .build();
            return new ResponseEntity<>(exceptionOutput, HttpStatus.CONFLICT);
        }

        final String token = usersAccountService.generateToken(createdUserAccount.getUsername());

        final SigninOutput signupOutput = SigninOutput.builder()
                .username(createdUserAccount.getUsername())
                .token(token)
                .build();

        return new ResponseEntity<>(signupOutput, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninInput signInInput) {
        final UserAccount userAccount = usersAccountService.queryByUsername(signInInput.getUsername());

        if (userAccount == null) {
            final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                    .message("User does not exist.")
                    .build();
            return new ResponseEntity<>(exceptionOutput, HttpStatus.NOT_FOUND);
        }

        if (!userAccount.getPassword().equals(signInInput.getPassword())) {
            final ExceptionOutput exceptionOutput = ExceptionOutput.builder()
                    .message("Password is incorrect.")
                    .build();
            return new ResponseEntity<>(exceptionOutput, HttpStatus.FORBIDDEN);
        }

        final String token = usersAccountService.generateToken(userAccount.getUsername());

        final SigninOutput signupOutput = SigninOutput.builder()
                .username(userAccount.getUsername())
                .token(token)
                .build();

        return new ResponseEntity<>(signupOutput, HttpStatus.OK);
    }
}
