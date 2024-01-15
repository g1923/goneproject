package com.javatechie.chatgptbot.config;

import org.springframework.web.servlet.HandlerInterceptor;

import com.javatechie.chatgptbot.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        if (requestURI.indexOf(".") == -1) {
            System.out.print(
                    "\u001B[32m 요청 데이터 Method=" + request.getRequestURI() + ", ip=" + Util.etRemoteAddr(request) + "("
                            + request.getMethod() + ") ");
        }

        return true;
    }
}
