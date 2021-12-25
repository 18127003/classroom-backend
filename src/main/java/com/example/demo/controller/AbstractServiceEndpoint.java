package com.example.demo.controller;

import java.util.Set;

public class AbstractServiceEndpoint {
    public static final String WEBAPP_API_PATH = "/api";

    public static final String ADMIN_PATH = "/admin";

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
            ASSIGNMENT_PATH + "**update**",
            ASSIGNMENT_PATH + "**updatePosition",
            ASSIGNMENT_PATH + "**studentInfo/**",
            ASSIGNMENT_PATH + "**submission/import",
            ASSIGNMENT_PATH + "**submission/**/update",
            ASSIGNMENT_PATH + "**submission/create**",
            ASSIGNMENT_PATH + "**template/export**",
            ASSIGNMENT_PATH + "**submission/finalize**",
            ASSIGNMENT_PATH + "**submission/filled**"
    );

    public static final Set<String> STUDENT_PROTECTED_PATH = Set.of(
            CLASS_PATH + "**participant/studentId/update",
            ASSIGNMENT_PATH + "**overallGrade**",
            ASSIGNMENT_PATH + "**submission/all**",
            ASSIGNMENT_PATH + "**submission/review/create"
    );

    public static final Set<String> ADMIN_PROTECTED_PATH = Set.of(
            ADMIN_PATH + "**"
    );
}
