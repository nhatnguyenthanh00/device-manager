export const API_ENDPOINT = {
    USER: '/api/admin/user',
    DEVICE: '/api/admin/device',
    USER_DETAIL: '/api/admin/user-detail',
    CHANGE_PASSWORD: '/api/change-password',
    RESET_PASWORD: '/api/admin/reset-password',
    LOGIN: '/api/login',
    PROFILE: '/api/profile',
    MY_DEVICE: '/api/my-device',
    DEVICE_RETURN: '/api/device-return',
    ACCEPT_RETUEN: '/api/admin/accept-return',
    REFUSE_RETURN: '/api/admin/refuse-return',
    USER_SELECT: '/api/admin/user-select',
} as const ;

export const ROUTES = {
    LOGIN: 'login',
    HOME: 'homepage',
    ADMIN_HOME: 'admin/homepage',
    FORBIDDEN: 'unauthorized',
} as const ;

export const STORAGE_KEYS = {
    TOKEN: 'currentToken',
} as const ;

export const ROLES = {
    ROLE_ADMIN: 'ROLE_ADMIN',
    ROLE_USER: 'ROLE_USER',
} as const ;

export const NUMBERS = {
    NUMBER_15: 15,
    NUMBER_100: 100,
    NUMBER_10000: 10000,
}