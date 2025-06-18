package com.example.demo.auth.factory;

import com.example.demo.auth.domain.LoginHistory;
import com.example.demo.auth.domain.UserInfo;

import java.time.LocalDateTime;

public class LoginHistoryFactory {
    public static LoginHistory create(UserInfo user, String ipAddress) {
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setLoginAt(LocalDateTime.now());
        history.setIpAddress(ipAddress);
        return history;
    }
}
