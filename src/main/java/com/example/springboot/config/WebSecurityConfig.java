package com.example.springboot.config;

import com.example.springboot.portal.entity.User;
import com.example.springboot.security.AuthenticationUser;
import com.example.springboot.security.SecurityUserHolder;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    // 更换CAS中的session中的key
    public final static String SESSION_KEY = AbstractCasFilter.CONST_CAS_ASSERTION;

    // 普通登录SESSION
    public final static String SESSION_LOGIN = "SESSION_LOGIN";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        //设定匹配的优先级

//        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/api/user/login/**");
        addInterceptor.excludePathPatterns("/api/user/caslogin/**");

        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            System.out.println("requst path " + request.getServletPath());
            HttpSession session = request.getSession(false);
            String jsessionId = null;
            if (session != null) {
                Cookie[] cookies = request.getCookies();
                for(Cookie c : cookies){
                    if(c.getName().equals("JSESSIONID")){
                        jsessionId = c.getValue();
                        System.out.println("JSESSIONID cookie found " + jsessionId);
                        break;
                    }
                }
                if(StringUtils.isNotBlank(jsessionId)){
                    if(jsessionId.equals(session.getId())){
                        System.out.println("jsessionid matches and session found :" + jsessionId);
                        Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
                        if (assertion != null) {
                            System.out.println("cas user ---------> " + assertion.getPrincipal().getName());
                        }
                        return true;
                    }
                }

//                AuthenticationUser user = SecurityUserHolder.getCurrentUser();
                Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);

                if (assertion != null) {
                    System.out.println("cas user ---------> " + assertion.getPrincipal().getName());
                }

                AuthenticationUser value = (AuthenticationUser) session.getAttribute(SESSION_LOGIN);

                System.out.println("security session = null ---------> " + (value == null));

                if (value != null) {
                    return true;
                }

            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
