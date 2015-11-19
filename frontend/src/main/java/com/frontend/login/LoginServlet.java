package com.frontend.login;

import com.frontend.FrontEndConstants;
import com.frontend.cookie.CookieHelper;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by ulises on 10/11/15.
 */
public class LoginServlet extends HttpServlet {

    private final Logger log = Logger.getLogger(LoginServlet.class.getName());

    private final LoginServices loginServices;

    public LoginServlet(LoginServices loginServices) {
        this.loginServices = loginServices;
    }

    @Override
    public void init() throws ServletException {
        log.info("LoginServlet init");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("LoginServlet doPost");
        String body;
        try (BufferedReader reader = req.getReader()) {
            body = reader.readLine();
            body = body.replace("{","").replace("}","").replace("\"","");
            String[] vars = body.split(",");
            String userId = vars[0].split(":")[1];
            String password = vars[1].split(":")[1];

            resp.setStatus(401);

            loginServices.login(userId, password).ifPresent(session -> {
                //TODO: encrypt cookie
                Cookie sessionCookie = CookieHelper.newCookie(30 * 60,session);
                resp.addCookie(sessionCookie);
                resp.setStatus(200);
            });



        }
    }
}
