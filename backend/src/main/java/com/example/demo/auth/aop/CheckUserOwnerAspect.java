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

import java.lang.reflect.InvocationTargetException;

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
        Long loggedInUserId = loginUser.getUserId();

        for (Object arg : joinPoint.getArgs()) {

            if (arg instanceof Long targetUserId) {
                if (!targetUserId.equals(loggedInUserId)) {
                    throw new AccessDeniedException("403 Forbidden: 다른 사용자의 데이터 접근 불가");
                }
                break;
            }

            try {
                Long targetId = (Long) arg.getClass().getMethod("getUserId").invoke(arg);
                if (!targetId.equals(loggedInUserId)) {
                    throw new AccessDeniedException("403 Forbidden: 다른 사용자의 데이터 접근 불가");
                }
                break;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                // getUserId 메서드 없으면 무시
            }
        }

        return joinPoint.proceed();
    }
}