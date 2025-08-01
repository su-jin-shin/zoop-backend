package com.example.demo.auth.aop;

import com.example.demo.auth.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

import static com.example.demo.common.response.FailedMessage.UNAUTHORIZED_ACCESS;
import static com.example.demo.common.response.FailedMessage.USER_NOT_FOUND;

@Aspect
@Component
@Slf4j
public class CheckUserOwnerAspect {

    @Around("@annotation(com.example.demo.auth.annotation.CheckUserOwner)")
    public Object checkUserOwnership(ProceedingJoinPoint joinPoint) throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth.getPrincipal() instanceof LoginUser loginUser)) {
            throw new AccessDeniedException(USER_NOT_FOUND.getMessage());
        }

        /* ───── LoginUser 에 getUserId()가 없으므로
                 getUsername() → "userId" 문자열을 Long 으로 파싱 ───── */
        Long loggedInUserId;
        try {
            loggedInUserId = Long.parseLong(loginUser.getUsername());
        } catch (NumberFormatException e) {
            throw new AccessDeniedException(USER_NOT_FOUND.getMessage());
        }

        /* ---------- 메서드 인자 순회 ---------- */
        for (Object arg : joinPoint.getArgs()) {

            // 1) Long userId 직접 전달
            if (arg instanceof Long targetUserId) {
                if (!targetUserId.equals(loggedInUserId)) {
                    throw new AccessDeniedException(UNAUTHORIZED_ACCESS.getMessage());
                }
                break;
            }

            // 2) DTO·Entity 안에 getUserId() 가 있는 경우
            try {
                Long targetId = (Long) arg.getClass().getMethod("getUserId").invoke(arg);
                if (!targetId.equals(loggedInUserId)) {
                    throw new AccessDeniedException("403 Forbidden: 다른 사용자의 데이터 접근 불가");
                }
                break;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                // getUserId 메서드가 없으면 무시
            }
        }

        return joinPoint.proceed();
    }
}
