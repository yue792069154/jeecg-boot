import {
    getAction,
    deleteAction,
    putAction,
    postAction
} from './action';

import appSettings from "../common/appSettings";

//登陆、注销
const LOGIN_SERVICE = (params) => postAction("/sys/login", appSettings.jeecg.serviceSiteRootUrl, params);
const LOGIN_OUT_SERVICE = (params) => postAction("/sys/logout", appSettings.jeecg.serviceSiteRootUrl, params);

//获取验证码
const CHECK_CODE_SERVICE = (params) => getAction("/sys/getCheckCode", appSettings.jeecg.serviceSiteRootUrl, params);

//获取菜单
const PERMSSION_LIST_SERVICE = (params) => getAction("/sys/permission/getUserPermissionByToken", appSettings.jeecg.serviceSiteRootUrl, params);

//用户
const USER_LIST_SERVICE = (params) => getAction("/sys/user/list", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ADD_SERVICE = (params) => postAction("/sys/user/add", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_EDIT_SERVICE = (params) => putAction("/sys/user/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_SERVICE = (params) => deleteAction("/sys/user/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/user/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ONLY_SERVICE = (params) => getAction("/sys/user/checkOnlyUser", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_BATCH_SERVICE = (params) => putAction("/sys/user/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_CHANGE_PASSWORD_SERVICE = (params) => putAction("/sys/user/changPassword", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_EXCEL_EXPORT_SERVICE = (params) => putAction("/sys/user/exportXls", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_EXCEL_IMPORT_SERVICE = (params) => putAction("sys/user/importExcel", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ROLE_LIST_SERVICE = (params) => getAction("/sys/user/queryUserRole", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DEPART_LIST_SERVICE = (params) => getAction("/sys/user/userDepartList", appSettings.jeecg.serviceSiteRootUrl, params);

const USER_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/user/exportXls";
const USER_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/user/importExcel";


//角色
const ROLE_ALL_LIST_SERVICE = (params) => getAction("/sys/role/queryall", appSettings.jeecg.serviceSiteRootUrl, params);


//职务
const POSITION_ALL_LIST_SERVICE = (params) => getAction("/sys/position/queryall", appSettings.jeecg.serviceSiteRootUrl, params);

//重复校验
const DUPLICATE_CHECK_SERVICE = (params) => getAction("/sys/duplicate/check", appSettings.jeecg.serviceSiteRootUrl, params);

//文件上传
const FILE_UPLOAD_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/upload";
const FILE_VIEW_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/view/";

export {

    LOGIN_SERVICE,
    LOGIN_OUT_SERVICE,
    CHECK_CODE_SERVICE,
    PERMSSION_LIST_SERVICE,

    USER_LIST_SERVICE,
    USER_ADD_SERVICE,
    USER_EDIT_SERVICE,
    USER_ROLE_LIST_SERVICE,
    USER_DEPART_LIST_SERVICE,
    USER_ONLY_SERVICE,
    USER_CHANGE_PASSWORD_SERVICE,
    USER_BATCH_SERVICE,
    USER_DELETE_SERVICE,
    USER_DELETE_BATCH_SERVICE,
    USER_EXCEL_EXPORT_SERVICE,
    USER_EXCEL_IMPORT_SERVICE,
    USER_EXPORT_SERVICE_URL,
    USER_IMPORT_SERVICE_URL,

    ROLE_ALL_LIST_SERVICE,

    POSITION_ALL_LIST_SERVICE,

    DUPLICATE_CHECK_SERVICE,

    FILE_UPLOAD_SERVICE_URL,
    FILE_VIEW_SERVICE_URL
};