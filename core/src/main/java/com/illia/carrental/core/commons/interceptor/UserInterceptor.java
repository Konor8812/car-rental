package com.illia.carrental.core.commons.interceptor;

import com.illia.carrental.core.commons.context.RequestUserContext;
import com.illia.carrental.core.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {

    private final AuthenticationService authenticationService;
    private final RequestUserContext userContext;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            return true;
        }
        var token = req.getHeader("authorization");
        var user = authenticationService.authUserByHeader(token);
        userContext.setUser(user);
        return true;
    }
}