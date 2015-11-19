package com.frontend.cookie;

import com.frontend.FrontEndConstants;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by ulises on 10/11/15.
 */
public class CookieHelper {


    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<String> parseSessionId(String session) {
        String[] vars = session.split(",");
        String sessionVar = vars[1].split(":")[1];
        return Optional.of(sessionVar.replace("\"", ""));
    }

    public static Cookie newCookie(int maxAge,String session){
        Cookie sessionCookie = new Cookie(FrontEndConstants.SESSION_COOKIE_NAME, session);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(maxAge);
        return sessionCookie;

    }

}
