package com.neu.easypay.service.impl;

import com.neu.easypay.exception.DuplicateException;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @Mock
    UserAccountMapper userAccountMapper;

    @InjectMocks
    UserAccountService userAccountService;

    @Mock
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
    void create() throws DuplicateException {
        when(userAccountMapper.queryByUsernameOrEmail(anyString(), anyString())).thenReturn(null);
        when(userAccountMapper.queryByUsername(anyString())).thenReturn(userAccount);
        UserAccount actual = userAccountService.create(userAccount);
        assertEquals(userAccount, actual);
    }

    @Test
    void createExceptionTest() {
        when(userAccountMapper.queryByUsernameOrEmail(anyString(), anyString())).thenReturn(userAccount);
        assertThrows(DuplicateException.class, () -> userAccountService.create(userAccount));
    }

    @Test
    void queryByUsername() {
        when(userAccountMapper.queryByUsername(any())).thenReturn(userAccount);
        UserAccount ua = userAccountService.queryByUsername("jy");
        assertEquals(userAccount.getUsername(), ua.getUsername());
    }

    @Test
    void queryByUuid() {
        when(userAccountMapper.queryByUuid(any())).thenReturn(userAccount);
        UserAccount ua = userAccountService.queryByUuid("2222");
        assertEquals(userAccount.getUsername(), ua.getUsername());
    }

    @Test
    void tokenTest() {
        userAccountService.setSecretKey("Tk9SVEhFQVNURVJOLVVOSVZFUlNJVFktU0VBVFRMRS0yMDIyRkFDUzU1MDBTRUEtU1VQRVItU0VDVVJFLVNFQ1JFVC1LRVk=");
        String token = userAccountService.generateToken("test-username");
        String username = userAccountService.decryptToken(token);

        assertEquals("test-username", username);
    }
}
