package com.neu.easypay.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Table;
@Data
@AllArgsConstructor
@Builder
@Table(name = "users")
public class BalanceResponse {
    private String name;
    private double balance;
}
