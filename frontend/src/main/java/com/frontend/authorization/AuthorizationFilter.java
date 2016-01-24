package com.frontend.authorization;

import com.frontend.FrontEndConstants;
import com.frontend.cookie.CookieHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by ulises on 24/10/15.
 */
public class AuthorizationFilter implements Filter {

    private final Logger log = Logger.getLogger(AuthorizationFilter.class.getName());

    private final AuthorizationServices authorizationServices;

    public AuthorizationFilter(AuthorizationServices authorizationServices) {
        this.authorizationServices = authorizationServices;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("AuthorizationFilter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("doFilter");


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        Optional<String> session = CookieHelper.getCookieValue(httpServletRequest, FrontEndConstants.SESSION_COOKIE_NAME)
                .flatMap(s -> CookieHelper.parseSessionId(s));


        if (session.isPresent() && authorizationServices
                .authorize(httpServletRequest.getRequestURI().toString(), session.get())) {

            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login/login.html");
        }


    }

    @Override
    public void destroy() {
        log.info("AuthorizationFilter destroy");
    }


}
