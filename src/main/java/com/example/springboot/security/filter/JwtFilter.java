package com.example.springboot.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class JwtFilter extends GenericFilterBean {

    private String secret = "aUBHMUwwghi_8LaAdS98LmVHBk99lJZP29JhQMzdRYa1thh4f1JTF5q3NDW5HQQMTw32RLvka0hNTgslmyUTCQ";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwtToken = request.getHeader("authorization");
//        String jwtToken = request.getParameter("ticket");
        System.out.println("begin======>");
        System.out.println(jwtToken);
        if(StringUtils.isNotBlank(jwtToken)){
            Jws<Claims> jws = Jwts.parser().setSigningKey(secret.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(jwtToken.replace("Bearer", ""));
            Claims claims = jws.getBody();
            String username = claims.getSubject();
            String st = String.valueOf(claims.get("jti"));
//            request.setAttribute("ticket", st);
//        AuthorityUtils.commaSeparatedStringToAuthorityList()
            System.out.println(username);
            System.out.println(st);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, st);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
