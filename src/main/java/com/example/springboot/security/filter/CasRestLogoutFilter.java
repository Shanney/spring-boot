package com.example.springboot.security.filter;

import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CasRestLogoutFilter extends GenericFilterBean {
    private RequestMatcher logoutRequestMatcher;

    private final LogoutHandler handler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * Constructor which takes a <tt>LogoutSuccessHandler</tt> instance to determine the
     * target destination after logging out. The list of <tt>LogoutHandler</tt>s are
     * intended to perform the actual logout functionality (such as clearing the security
     * context, invalidating the session, etc.).
     */
    public CasRestLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
        this.handler = new CompositeLogoutHandler(handlers);
        Assert.notNull(logoutSuccessHandler, "logoutSuccessHandler cannot be null");
        this.logoutSuccessHandler = logoutSuccessHandler;
        setFilterProcessesUrl("/logout");
    }

    public CasRestLogoutFilter(String logoutSuccessUrl, LogoutHandler... handlers) {
        this.handler = new CompositeLogoutHandler(handlers);
        Assert.isTrue(!StringUtils.hasLength(logoutSuccessUrl) || UrlUtils.isValidRedirectUrl(logoutSuccessUrl),
                () -> logoutSuccessUrl + " isn't a valid redirect URL");
        CasRestLogoutSuccessHandler casRestLogoutSuccessHandler = new CasRestLogoutSuccessHandler();
        if (StringUtils.hasText(logoutSuccessUrl)) {
            casRestLogoutSuccessHandler.setDefaultTargetUrl(logoutSuccessUrl);
        }
        this.logoutSuccessHandler = casRestLogoutSuccessHandler;
        setFilterProcessesUrl("/logout");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (requiresLogout(request, response)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Logging out [%s]", auth));
            }
            this.handler.logout(request, response, auth);
            this.logoutSuccessHandler.onLogoutSuccess(request, response, auth);
            return;
        }
        chain.doFilter(request, response);
    }

    /**
     * Allow subclasses to modify when a logout should take place.
     * @param request the request
     * @param response the response
     * @return <code>true</code> if logout should occur, <code>false</code> otherwise
     */
    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        if (this.logoutRequestMatcher.matches(request)) {
            return true;
        }
        if (this.logger.isTraceEnabled()) {
            this.logger.trace(LogMessage.format("Did not match request to %s", this.logoutRequestMatcher));
        }
        return false;
    }

    public void setLogoutRequestMatcher(RequestMatcher logoutRequestMatcher) {
        Assert.notNull(logoutRequestMatcher, "logoutRequestMatcher cannot be null");
        this.logoutRequestMatcher = logoutRequestMatcher;
    }

    public void setFilterProcessesUrl(String filterProcessesUrl) {
        this.logoutRequestMatcher = new AntPathRequestMatcher(filterProcessesUrl);
    }
}
