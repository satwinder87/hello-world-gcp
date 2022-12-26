package com.example.helloworldgcp.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Inside testFilter .... ");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        System.out.println("Logging Request " + request.getMethod() + " , " +  request.getRequestURI());

        System.out.println("AuthType = " + request.getAuthType());
        if(request.getCookies() != null && request.getCookies().length > 0){
            Arrays.stream(request.getCookies()).forEach(cookie -> System.out.println("Cookie = " + cookie.getName() + "," + cookie.getValue() + cookie.getSecure() + cookie.getMaxAge()));
        }
        System.out.println("Principal= " + request.getUserPrincipal());

        Iterator<String> stringIterator = request.getHeaderNames().asIterator();

        while(stringIterator.hasNext()){
            String header = stringIterator.next();
            System.out.println("Header name = " + header + ", Value = " + request.getHeader(header));
        }

        //call next filter in the filter chain
        filterChain.doFilter(request, response);

        System.out.println("Inside testFilter .... Exited");

    }
}
