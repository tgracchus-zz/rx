package com.backend.server.endpoint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexURIMatcher implements URIMatcher {

    private final Pattern pattern;

    public RegexURIMatcher(String pathExpression) {
        super();
        this.pattern = Pattern.compile(pathExpression);
    }

    @Override
    public boolean match(String uri) {
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();

    }

}
