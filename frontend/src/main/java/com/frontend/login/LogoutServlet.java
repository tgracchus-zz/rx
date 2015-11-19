package com.frontend.login;

import com.frontend.FrontEndConstants;
import com.frontend.cookie.CookieHelper;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ulises on 10/11/15.
 */
public class LogoutServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(LogoutServlet.class.getName());

    private final LoginServices loginServices;

    public LogoutServlet(LoginServices loginServices) {
        this.loginServices = loginServices;
    }

    @Override
    public void init() throws ServletException {
        log.info("LogoutServlet init");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("LogoutServlet doPost");

        resp.setStatus(400);

        CookieHelper.getCookieValue(req, FrontEndConstants.SESSION_COOKIE_NAME)
                .flatMap(s -> CookieHelper.parseSessionId(s)).ifPresent(s -> {
            loginServices.logout(s);
            Cookie deleteCookie = CookieHelper.newCookie(0, s);
            resp.addCookie(deleteCookie);
            resp.setStatus(200);
        });

    }

}
