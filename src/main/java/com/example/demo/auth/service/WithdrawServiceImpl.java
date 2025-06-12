package com.example.demo.auth.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.auth.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {

    private final UserInfoRepository userRepo;

    @Transactional
    @Override
    public void withdraw(Long userId, String reason) {
        UserInfo user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        user.withdraw(reason);          // 엔티티 책임에게 위임
    }
}
