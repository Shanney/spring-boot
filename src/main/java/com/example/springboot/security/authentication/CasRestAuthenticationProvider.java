package com.example.springboot.security.authentication;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class CasRestAuthenticationProvider implements AuthenticationProvider {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private final String service = "http://172.168.159.233:8080/auth/login";

    private TicketValidator ticketValidator;

    private AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService;

    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        if ((authentication.getCredentials() == null) || "".equals(authentication.getCredentials())) {
            throw new BadCredentialsException("Failed to provide a CAS password to validate");
        }
        // post请求，通过username,password获取TGT
        String tgt = getTgt(String.valueOf(authentication.getPrincipal()), String.valueOf(authentication.getCredentials().toString()));
        // get请求，通过TGT, service，获取该service对应的ST
        String st = getSt(tgt, service);
        // 通过service, st,进行最终校验，这步应该用security自己的一套方法
        CasRestAuthenticationToken result = null;
        result = this.authenticateNow(tgt, service, st);
        return result;
    }

    public String getTgt(String username, String password){
        RestTemplate client = new RestTemplate();
        client.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json;charset=UTF-8");
        headers.set("Accept", "application/json;charset=UTF-8");
        HttpMethod method = HttpMethod.POST;
        //以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<String, String>();
        params.add("username", username);
        params.add("password", password);
        //将请求头部和参数合成一个请求
        System.out.println("params===="+ params);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用RestVO类格式化
        ResponseEntity<String> response = client.exchange("https://www.trycas.com:8443/cas/v1/tickets", method, requestEntity, String.class);
        System.out.println("response=="+response.toString());

        if(response.getStatusCodeValue() != 201){
            throw new BadCredentialsException("Failed to get st with ticket and service");
        }
        return response.getBody();
    }

    public String getSt(String tgt, String service) {

        RestTemplate client = new RestTemplate();
        client.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json;charset=UTF-8");
        headers.set("Accept", "application/json;charset=UTF-8");
        HttpMethod method = HttpMethod.POST;
        //以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<String, String>();
        params.add("service", service);
        //将请求头部和参数合成一个请求
        System.out.println("params===="+ params);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用RestVO类格式化
        ResponseEntity<String> response = client.exchange("https://www.trycas.com:8443/cas/v1/tickets/"+tgt, method, requestEntity, String.class);
        System.out.println("response=="+response.toString());
        if(response.getStatusCodeValue() != 200){
            throw new BadCredentialsException("Failed to get st with ticket and service");
        }
        return response.getBody();
    }

    protected UserDetails loadUserByAssertion(final Assertion assertion) {
        final CasAssertionAuthenticationToken token = new CasAssertionAuthenticationToken(assertion, "");
        return this.authenticationUserDetailsService.loadUserDetails(token);
    }

    private CasRestAuthenticationToken authenticateNow(String tgt, String service, String st) throws AuthenticationException {
        try {
            Assertion assertion = this.ticketValidator.validate(st,service);
            UserDetails userDetails = loadUserByAssertion(assertion);
            this.userDetailsChecker.check(userDetails);
            return new CasRestAuthenticationToken(this.key, userDetails, st,
                    this.authoritiesMapper.mapAuthorities(userDetails.getAuthorities()), userDetails, assertion,tgt);
        }
        catch (TicketValidationException ex) {
            throw new BadCredentialsException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication))
                || (CasAuthenticationToken.class.isAssignableFrom(authentication))
                || (CasAssertionAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    public TicketValidator getTicketValidator() {
        return ticketValidator;
    }

    public void setTicketValidator(TicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService) {
        this.authenticationUserDetailsService = authenticationUserDetailsService;
    }
}
