package com.sample.web.base;

/**
 * 定数定義
 */
public interface WebConst {

    /** ---- MDC ---- **/
    String MDC_LOGIN_USER_ID = "LOGIN_USER_ID";

    String MDC_FUNCTION_NAME = "FUNCTION_NAME";

    /** ---- Message ---- **/
    String GLOBAL_MESSAGE = "GlobalMessage";

    String VALIDATION_ERROR = "ValidationError";

    String OPTIMISTIC_LOCKING_FAILURE_ERROR = "OptimisticLockingFailureError";

    String DOUBLE_SUBMIT_ERROR = "DoubleSubmitError";

    String DELETED = "Deleted";

    /** ---- View ---- **/
    String ERROR_VIEW = "error";

    String NOTFOUND_VIEW = "notfound";

    String FORBIDDEN_VIEW = "forbidden";

    /** ---- DateFormat ---- **/
    String LOCALDATE_FORMAT = "yyyy/MM/dd";

    String LOCALDATETIME_FORMAT = "yyyy/[]M/[]d []H:[]m:[]s";

    /** ---- ViewComponents ---- **/
    String MAV_CONST = "Const";

    String MAV_ERRORS = "errors";

    String MAV_PULLDOWN_OPTION = "PulldownOption";

    String MAV_CODE_CATEGORIES = "CodeCategories";

    /** ---- URLs ---- **/
    String HOME_URL = "/";

    String ERROR_URL = "/error";

    String NOTFOUND_URL = "/notfound";

    String FORBIDDEN_URL = "/forbidden";

    String LOGIN_URL = "/login";

    String LOGIN_PROCESSING_URL = "/authenticate";

    String LOGIN_SUCCESS_URL = "/loginSuccess";

    String LOGIN_FAILURE_URL = "/loginFailure";

    String LOGOUT_URL = "/logout";

    String WEBJARS_URL = "/webjars/**";

    String STATIC_RESOURCES_URL = "/static/**";
}
