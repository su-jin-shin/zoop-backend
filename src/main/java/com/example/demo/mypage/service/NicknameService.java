package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NicknameService {

    private final MypageUserInfoRepository mypageUserInfoRepository;

    public void updateNickname(Long userId, String nickname) {

        if (mypageUserInfoRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        UserInfo user = mypageUserInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
        user.setNickname(nickname);
        mypageUserInfoRepository.save(user);
    }


    public boolean isNicknameDuplicate(String nickname) {

        return mypageUserInfoRepository.existsByNickname(nickname);
    }
}
