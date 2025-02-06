package com.example.server.utils.constants;

import java.util.List;

public class Constants {

    public static class ErrCode {
        public static final int ERROR_CODE_0 = 0;
        public static final int ERROR_CODE_401 = 401;
        public static final int ERROR_CODE_500 = 500;
    }

    public static class Common {

        public static final String EMPTY = "";

        public static final String NUMBER_1_STRING = "1";

        public static final String SPACE = " ";

        public static final int NUMBER_1_INT_NEGATIVE = -1;
        public static final int NUMBER_0_INT = 0;
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
        public static final String PROFILE_PATH = "/profile";
        public static final String CHANGE_PASSWORD_PATH = "/change-password";
        public static final String ADMIN_DEVICE_PATH = "/admin/device";
        public static final String MY_DEVICE_PATH = "/my-device";
        public static final String DEVICE_RETURN = "/device-return";
        public static final String ADMIN_ACCEPT_RETURN = "/admin/accept-return";
        public static final String ADMIN_REFUSE_RETURN = "/admin/refuse-return";
        public static final String EMPTY_PATH = "/";

        public static final String ADMIN_USER_PATH = "/admin/user";
        public static final String ADMIN_RESET_PASSWORD = "/admin/reset-password";
        public static final String ADMIN_USER_DETAIL = "/admin/user-detail";
        public static final String ADMIN_USER_SELECT = "/admin/user-select";
        public static final String USER_PATH = "/user";
        public static final String DEVICE_PATH = "/device";
    }

    public static class ErrorMessage {
        public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
        public static final String INCORRECT_PASSWORD = "Password incorrect";
        public static final String SYSTEM_BUSY = "Internal Server Error";
        public static final String FORBIDDEN_ERROR = "You don't have permission";
        public static final String NEW_PASSWORD_SAME_OLD_PASSWORD = "New password is same old password";
        public static final String ADMIN_PASSWORD_INCORRECT = "Admin password incorrect";
        public static final String NOT_FOUND_USER = "Not found user";
        public static final String NOT_FOUND_DEVICE = "Not found device";
        public static final String DEVICE_ASSIGNED = "Device assigned to someone";

        public static final String REQUEST_RETURN_FAIL = "Request return fail";
        public static final String ACCEPT_RETURN_FAIL = "Accept return fail";
        public static final String REFUSE_RETURN_FAIL = "Refuse return fail";

        public static final String USER_NOW_CONTROL_DEVICES = "User are having some device";
        public static final String INVALID_INPUT = "Invalid input";
        public static final String USERNAME_EXISTED = "Username already existed";
        public static final String NAME_EXISTED = "Name already existed";
    }

    public static class RegexString {
        public static final String NAME_REGEX = "^[\\p{L}\\s]+$";  // Matches letters (including Vietnamese) and spaces
        public static final String USERNAME_REGEX = "^[a-zA-Z0-9]{6,}$";  // Alphanumeric, min 6 chars, no spaces
        public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,}$";
    }
}
