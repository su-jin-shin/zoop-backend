package com.example.demo.auth.aop;

import com.example.demo.auth.annotation.CheckUserOwner;
import com.example.demo.auth.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CheckUserOwnerAspect {

    @Around("@annotation(com.example.demo.auth.annotation.CheckUserOwner)")
    public Object checkUserOwnership(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth.getPrincipal() instanceof LoginUser loginUser)) {
            throw new AccessDeniedException("401 Unauthorized: 인증 정보가 올바르지 않습니다.");
        }
        Long loggedInUserId = loginUser.getUserId();   //  userId 추출

        /* ---------- 메서드 인자 순회 ---------- */
        for (Object arg : joinPoint.getArgs()) {

            // 1) Long userId 직접 전달
            if (arg instanceof Long targetUserId) {
                if (!targetUserId.equals(loggedInUserId)) {
                    throw new AccessDeniedException("403 Forbidden: 다른 사용자의 데이터 접근 불가");
                }
                break;
            }

            // 2) Entity·DTO 안에 getUserId()가 있는 경우 리플렉션 검사 (선택)
            try {
                Long targetId = (Long) arg.getClass().getMethod("getUserId").invoke(arg);
                if (!targetId.equals(loggedInUserId)) {
                    throw new AccessDeniedException("403 Forbidden: 다른 사용자의 데이터 접근 불가");
                }
                break;
            } catch (Exception ignored) { /* 해당 메서드가 없으면 그냥 패스 */ }
        }

        return joinPoint.proceed();
    }
}
