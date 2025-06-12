package com.example.demo.auth.service;

import jakarta.transaction.Transactional;

public interface WithdrawService {
    void withdraw(Long userId, String reason);
}
