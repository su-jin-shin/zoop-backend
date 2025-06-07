package com.example.demo.mypage.service;

import com.example.demo.auth.domain.UserInfo;
import com.example.demo.mypage.repository.MypageUserInfoRepository;
import com.sun.jdi.request.DuplicateRequestException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class NicknameService {

    private final MypageUserInfoRepository mypageUserInfoRepository;

    public void updateNickname(Long userId, String nickname) {

        if (mypageUserInfoRepository.existsByNickname(nickname)) {
            throw new DuplicateRequestException("이미 사용 중인 닉네임입니다.");
        }
        UserInfo user = mypageUserInfoRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
        user.setNickname(nickname);
        mypageUserInfoRepository.save(user);
    }


    public boolean isNicknameDuplicate(
            @NotBlank(message = "닉네임을 입력해주세요.")
            @Size(min = 1, max = 20, message = "닉네임을 1~20자 범위 내로 입력해주세요.")
            @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 특수문자와 공백을 포함할 수 없습니다.") String nickname) {

        return mypageUserInfoRepository.existsByNickname(nickname);
    }
}
