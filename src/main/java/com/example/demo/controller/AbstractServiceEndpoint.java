package com.example.demo.controller;

import java.util.Set;

public class AbstractServiceEndpoint {
    public static final String WEBAPP_API_PATH = "/api";

    public static final String CLASS_PATH = WEBAPP_API_PATH + "/classroom/";

    public static final String ACCOUNT_PATH = WEBAPP_API_PATH + "/account/";

    public static final String AUTH_PATH = WEBAPP_API_PATH + "/auth/";

    public static final Set<String> TEACHER_PROTECTED_PATH = Set.of(
            CLASS_PATH + "**/invite**"
    );

    public static final Set<String> STUDENT_PROTECTED_PATH = Set.of(
            CLASS_PATH + "**/test**"
    );
}
