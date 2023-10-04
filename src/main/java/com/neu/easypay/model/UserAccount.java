package com.neu.easypay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserAccount {
    private String id;
    private String uuid;
    private String username;
    private String email;
    private String name;
    private String password;
    private Date timestamp;
    private double balance;
}
