package com.example.springboot.security.filter;

import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CasRestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Used to identify a CAS request for a stateful user agent, such as a web browser.
     */
    public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";

    /**
     * Used to identify a CAS request for a stateless user agent, such as a remoting
     * protocol client (e.g. Hessian, Burlap, SOAP etc). Results in a more aggressive
     * caching strategy being used, as the absence of a <code>HttpSession</code> will
     * result in a new authentication attempt on every request.
     */
    public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    private String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/login",
            "POST");

    public CasRestAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter("username");
        username = (username != null) ? username : "";
        username = username.trim();
        String password = request.getParameter("password");
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * If present, gets the artifact (CAS ticket) from the {@link HttpServletRequest}.
     * @param request
     * @return if present the artifact from the {@link HttpServletRequest}, else null
     */
    protected String obtainArtifact(HttpServletRequest request) {
        return request.getParameter(this.artifactParameter);
    }

    /**
     * Indicates if the request is elgible to process a service ticket. This method exists
     * for readability.
     * @param request
     * @param response
     * @return
     */
    private boolean serviceTicketRequest(HttpServletRequest request, HttpServletResponse response) {
        boolean result = super.requiresAuthentication(request, response);
        this.logger.debug(LogMessage.format("serviceTicketRequest = %s", result));
        return result;
    }

}
