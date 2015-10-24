package com.schibsted.frontend.authorization;

import java.util.Optional;

/**
 * Created by ulises on 24/10/15.
 */
public class RolDecider {


    public Optional<String> parseRol(String requestUri) {
        int start = requestUri.lastIndexOf("/") + 1;
        int end = requestUri.lastIndexOf(".html");
        String rol = null;
        if (end >= 0 && start >= 0) {
            String page = requestUri.substring(start, end);

            if (page.equals("page1")) {
                rol = "PAGE_1";
            } else if (page.equals("page2")) {
                rol = "PAGE_2";
            } else if (page.equals("page3")) {
                rol = "PAGE_3";
            }
        }
        return Optional.ofNullable(rol);
    }
}
