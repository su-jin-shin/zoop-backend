package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.common.exception.DuplicatedNicknameException;
import com.example.demo.common.response.FailedMessage;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.common.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class NicknameService {

    private final MypageUserInfoRepository mypageUserInfoRepository;

    public void updateNickname(Long userId, String nickname) {
        if (mypageUserInfoRepository.existsByNickname(nickname)) {
            throw new DuplicatedNicknameException();
        }
        UserInfo user = mypageUserInfoRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setNickname(nickname);
        mypageUserInfoRepository.save(user);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return mypageUserInfoRepository.existsByNickname(nickname);
    }
}
