package com.neu.easypay.service.impl;

import com.neu.easypay.exception.DuplicateException;
import com.neu.easypay.mapper.UserAccountMapper;
import com.neu.easypay.model.UserAccount;
import com.neu.easypay.service.IUserAccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserAccountService implements IUserAccountService {

    private final @NonNull UserAccountMapper userAccountMapper;

    @Value("${authentication.jwt.secret.key}")
    private String SECRET_KEY;

    public void setSecretKey(String key) {
        SECRET_KEY = key;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 864000000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String decryptToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public UserAccount create(UserAccount userAccount) throws DuplicateException {
        // check if there is an existing account with the same username or email
        if (queryByUsernameOrEmail(userAccount.getUsername(), userAccount.getEmail()) != null) {
            throw new DuplicateException();
        }

        userAccountMapper.create(userAccount);

        return queryByUsername(userAccount.getUsername());
    }

    @Override
    public UserAccount queryByUsername(String username) {
        return userAccountMapper.queryByUsername(username);
    }

    private UserAccount queryByUsernameOrEmail(String username, String email) {
        return userAccountMapper.queryByUsernameOrEmail(username, email);
    }

    public UserAccount queryByUuid(String uuid) {
        return userAccountMapper.queryByUuid(uuid);
    }
}
