package com.example.demo.controller;

import java.util.Set;

public class AbstractServiceEndpoint {
    public static final String WEBAPP_API_PATH = "/api";

    public static final String CLASS_PATH = WEBAPP_API_PATH + "/classroom/";

    public static final String ACCOUNT_PATH = WEBAPP_API_PATH + "/account/";

    public static final String ASSIGNMENT_PATH = CLASS_PATH + "**/assignment/";

    public static final String AUTH_PATH = WEBAPP_API_PATH + "/auth/";

    public static final Set<String> TEACHER_PROTECTED_PATH = Set.of(
            CLASS_PATH + "**/invite**",
            CLASS_PATH + "**/removeParticipants**",
            CLASS_PATH + "**/hideParticipants**",
            CLASS_PATH + "**/regenerateCode**",
            ASSIGNMENT_PATH + "**create**",
            ASSIGNMENT_PATH + "**remove**",
            ASSIGNMENT_PATH + "**update**"
    );

    public static final Set<String> STUDENT_PROTECTED_PATH = Set.of(
            CLASS_PATH + "**/test**"
    );
}
