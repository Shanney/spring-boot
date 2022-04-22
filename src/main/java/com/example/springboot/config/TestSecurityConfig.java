package com.example.springboot.config;

import com.example.springboot.security.AuthenticationUser;
import com.example.springboot.security.UserDetailServiceImpl;
import com.example.springboot.security.authentication.CasRestAuthenticationEntryPoint;
import com.example.springboot.security.authentication.CasRestAuthenticationProvider;
import com.example.springboot.security.authentication.CasRestAuthenticationToken;
import com.example.springboot.security.filter.CasRestAuthenticationFilter;
import com.example.springboot.security.filter.CasRestLogoutFilter;
import com.example.springboot.security.filter.JwtFilter;
import com.example.springboot.security.validation.CasRestServiceTicketValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CasProperties casProperties;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/img/**","/*.ico","/login.html",
        "/error","/login.do");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.authenticationProvider(casRestAuthenticationProvider());
     }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and().logout().permitAll().invalidateHttpSession(true)
//                .and()
//                .antMatcher("/auth/login").authorizeRequests()
//                .rememberMe()
//                .tokenValiditySeconds(1209600)
                .and()
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .authorizeRequests()
//                .antMatchers("/**")
//                .hasRole("USER");
                .anyRequest()
                .authenticated();

//                .fullyAuthenticated();
        http.exceptionHandling()
//                .authenticationEntryPoint(new Http403ForbiddenEntryPoint()) //这个是pre-authentication，如果设置成了403，所有请求都没有反应了
                .authenticationEntryPoint(casRestAuthenticationEntryPoint())
                .and()
                .addFilterBefore(casRestAuthenticationFilter(), CasAuthenticationFilter.class)
                .addFilterBefore(casRestLogoutFilter(),LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter(),CasRestAuthenticationFilter.class);
//        http.csrf().disable();
//        http.headers().frameOptions().sameOrigin();
//        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();

    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(){
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasSererLoginUrl());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    @Bean
    public ServiceProperties serviceProperties(){
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casProperties.getAppServerUrl()+casProperties.getAppLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    public CasRestAuthenticationEntryPoint casRestAuthenticationEntryPoint(){
        CasRestAuthenticationEntryPoint casRestAuthenticationEntryPoint = new CasRestAuthenticationEntryPoint();
        return casRestAuthenticationEntryPoint;
    }

    /**
     * cas认证过滤器
     * casAuthenticationFilter.setFilterProcessesUrl 必须要设置完整路径 不然会无限重定向
     */
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter=new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppServerUrl()+casProperties.getAppLoginUrl());
        casAuthenticationFilter.setServiceProperties(serviceProperties());
        //        casAuthenticationFilter.setAuthenticationSuccessHandler(
        //                new SimpleUrlAuthenticationSuccessHandler("/home.html"));
        // 这里用了一个lamda表达式，正常也可以写一个AuthenticationSuccessHandler类，实例化之后作为参数传递进去
        casAuthenticationFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            Object principal = authentication.getPrincipal();
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper().writeValueAsString(principal));
            out.close();
        }));
        return casAuthenticationFilter;
    }

    public CasRestAuthenticationFilter casRestAuthenticationFilter() throws Exception {
        CasRestAuthenticationFilter casRestAuthenticationFilter = new CasRestAuthenticationFilter();
        casRestAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casRestAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
        casRestAuthenticationFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            AuthenticationUser principal = (AuthenticationUser) authentication.getPrincipal();
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("username", principal.getUsername());
            rootNode.put("ssoTgt", ((CasRestAuthenticationToken) authentication).getSsoTgt());
//            out.write(new ObjectMapper().writeValueAsString(principal));
            out.write(rootNode.toString());
            out.close();
        }));
        casRestAuthenticationFilter.setAuthenticationFailureHandler(((request, response, authentication) -> {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("code", 401);
            rootNode.put("msg", "Authentication failure, re-login required.");
            out.write(rootNode.toString());
            out.close();
        }));
        return casRestAuthenticationFilter;
    }

    /**
     * cas认证Provider
     */
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(){
        CasAuthenticationProvider casAuthenticationProvider=new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(userDetailService);
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

    @Bean
    public CasRestAuthenticationProvider casRestAuthenticationProvider(){
        CasRestAuthenticationProvider casRestAuthenticationProvider = new CasRestAuthenticationProvider();
        casRestAuthenticationProvider.setAuthenticationUserDetailsService(userDetailService);
        casRestAuthenticationProvider.setTicketValidator(casRestServiceTicketValidator());
        casRestAuthenticationProvider.setKey("casRestAuthenticationProviderKey");
        return casRestAuthenticationProvider;
    }

    public CasRestLogoutFilter casRestLogoutFilter(){
        CasRestLogoutFilter logoutFilter = new CasRestLogoutFilter(casProperties.getCasServerLogoutUrl(),
                new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
        return logoutFilter;
    }

    public SingleSignOutFilter singleSignOutFilter(){
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    @Bean
    public CasRestServiceTicketValidator casRestServiceTicketValidator(){
        return new CasRestServiceTicketValidator(casProperties.getCasServerUrl());
    }

}
