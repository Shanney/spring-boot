package com.example.springboot;

import com.example.springboot.config.WebSecurityConfig;
import com.example.springboot.security.AuthenticationUser;
import com.example.springboot.security.SecurityUserHolder;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class HelloController {

    @Autowired
    private UserDetailsService userDetailServiceImpl;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/greetings")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/qianbaoCreate")
    public String test(){
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        return user.getUsername() + ": successfully entering qianbaoCreate!";
    }

    @GetMapping("/api/testRest")
    public String testRest(){
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        return user.getUsername() + ": successfully entering api/testRest!";
    }

    @GetMapping("/api/testApi")
    public String testApi(){
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        return user.getUsername() + ": successfully entering api/testApi!";
    }

    @GetMapping(value = "/api/user/caslogin")
    public String casLogin(){

        HttpSession session = request.getSession();
        AuthenticationUser user = SecurityUserHolder.getCurrentUser();
        if (user != null) {
            session.setAttribute(WebSecurityConfig.SESSION_LOGIN, user);
            String jsessionid = session.getId();
            System.out.println("jsessionid ------> " + jsessionid);
            // 跳转到前端
//                response.sendRedirect("http://172.16.67.228:8000”);
            //这里不再跳转，而是返回一个json串，告诉前端登录成功
            return "login successfully!";
        }else{
            return "not login yet!";
        }
         /*
        Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        if (assertion != null) {

            if (user != null) {
                session.setAttribute(WebSecurityConfig.SESSION_LOGIN, user);
                // 跳转到前端
//                response.sendRedirect("http://172.16.67.228:8000”);
                //这里不再跳转，而是返回一个json串，告诉前端登录成功
                return "login successfully!";
            }
        }else{
            return "not login yet!";
        }

            //获取登录用户名
            String username = assertion.getPrincipal().getName();
            System.out.println("user ---------> " + username);
            AuthenticationUser temp = userDetailServiceImpl.loadUserByUsername(username);
            System.out.println("TEMP user ---------> " + (temp.getUsername()));
            if (temp != null) {
                session.setAttribute(WebSecurityConfig.SESSION_LOGIN, temp);
                // 跳转到前端
//                response.sendRedirect("http://172.16.67.228:8000”);
                //这里不再跳转，而是返回一个json串，告诉前端登录成功
                return "login successfully!";
            }
        }

         */
    }

}
