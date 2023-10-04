package com.neu.easypay.controller;

import com.neu.easypay.exception.DuplicateException;
import com.neu.easypay.model.SigninInput;
import com.neu.easypay.model.SigninOutput;
import com.neu.easypay.model.SignupInput;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.impl.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountControllerTest {

    @Mock
    UserAccountService userAccountService;

    @InjectMocks
    private UserAccountController controller;

    UserAccount userAccount;

    @BeforeEach
    void setUserAccount() {
        userAccount = UserAccount.builder()
                .id("1111")
                .uuid("2222")
                .username("jy")
                .email("jenny@gmail.com")
                .name("Jenny")
                .password("jenny123")
                .timestamp(new Date())
                .balance(0.0)
                .build();
    }

    @Test
    void signup() throws DuplicateException {
        SignupInput input = SignupInput.builder()
                .username("test-username")
                .email("test@email.com")
                .password("password")
                .name("name")
                .build();
        when(userAccountService.create(any(UserAccount.class))).thenReturn(userAccount);
        when(userAccountService.generateToken(anyString())).thenReturn("test-token");

        ResponseEntity<?> responseEntity = controller.signup(input);
        SigninOutput output = (SigninOutput) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("jy", output.getUsername());
        assertEquals("test-token", output.getToken());
    }

    @Test
    void signupExceptionTest() throws DuplicateException {
        SignupInput input = SignupInput.builder()
                .username("test-username")
                .email("test@email.com")
                .password("password")
                .name("name")
                .build();
        when(userAccountService.create(any(UserAccount.class))).thenThrow(DuplicateException.class);
        ResponseEntity<?> responseEntity = controller.signup(input);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void signinNotFoundTest() {
        SigninInput input = SigninInput.builder()
                .username("test-username")
                .password("password")
                .build();
        when(userAccountService.queryByUsername(anyString())).thenReturn(null);

        ResponseEntity<?> responseEntity = controller.signin(input);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void signinWrongPasswordTest() {
        SigninInput input = SigninInput.builder()
                .username("test-username")
                .password("password")
                .build();
        when(userAccountService.queryByUsername(anyString())).thenReturn(userAccount);

        ResponseEntity<?> responseEntity = controller.signin(input);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void signinTest() {
        SigninInput input = SigninInput.builder()
                .username("test-username")
                .password("jenny123")
                .build();
        when(userAccountService.queryByUsername(anyString())).thenReturn(userAccount);
        when(userAccountService.generateToken(anyString())).thenReturn("test-token");

        ResponseEntity<?> responseEntity = controller.signin(input);
        SigninOutput output = (SigninOutput) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("jy", output.getUsername());
        assertEquals("test-token", output.getToken());
    }
}
