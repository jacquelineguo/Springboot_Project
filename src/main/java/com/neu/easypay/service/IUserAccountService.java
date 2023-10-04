package com.neu.easypay.service;

import com.neu.easypay.exception.DuplicateException;
import com.neu.easypay.model.UserAccount;

public interface IUserAccountService {

    UserAccount create(UserAccount userAccount) throws DuplicateException;

    UserAccount queryByUsername(String username);
}
