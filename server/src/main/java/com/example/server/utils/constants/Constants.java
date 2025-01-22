package com.example.server.utils.constants;

import java.util.List;

public class Constants {

    public static class ErrCode {
        public static final int ERROR_CODE_0 = 0;
        public static final int ERROR_CODE_401 = 401;
        public static final int ERROR_CODE_500 = 500;
    }

    public static class Common {
        public static final String SPACE = " ";

        public static final int NUMBER_1_INT = 1;
        public static final int NUMBER_5_INT = 5;
        public static final int NUMBER_7_INT = 7;
        public static final int NUMBER_11_INT = 11;
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";

        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";

        public static final List<String> ALL_GENDER = List.of("MALE","FEMALE");
        public static final List<String> ALL_ROLE = List.of("USER","ADMIN");

        public static final List<String> ALL_CATEGORY = List.of("LAPTOP","PC","MOUSE","PHONE");

    }
    public static class Security {
        public static final String AUTHORIZATION = "Authorization";
        public static final String BEARER = "Bearer";
        public static final String ROLE_LOWERCASE = "role";
        public static final String ROLE_UNDER = "ROLE_";
    }

    public static class ApiEndpoint {
        public static final String ADMIN_BASE_PATH = "/api/admin";
        public static final String COMMON_BASE_PATH = "/api";
        public static final String LOGIN_PATH = "/login";
        public static final String EMPTY_PATH = "/";
        public static final String USER_PATH = "/user";
        public static final String DEVICE_PATH = "/device";
    }
}
