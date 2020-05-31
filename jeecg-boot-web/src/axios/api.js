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
 

//用户
const USER_LIST_SERVICE = (params) => postAction("/sys/sysUser/list", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ALL_LIST_SERVICE = (params) => postAction("/sys/sysUser/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ADD_SERVICE = (params) => postAction("/sys/sysUser/add", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_EDIT_SERVICE = (params) => putAction("/sys/sysUser/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_QUERY_SERVICE = (params) => getAction("/sys/sysUser/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_SERVICE = (params) => deleteAction("/sys/sysUser/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysUser/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_BATCH_SERVICE = (params) => putAction("/sys/user/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_CHANGE_PASSWORD_SERVICE = (params) => putAction("/sys/user/changPassword", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ROLE_ALL_LIST_SERVICE = (params) => postAction("/sys/sysUserRole/queryall", appSettings.jeecg.serviceSiteRootUrl, params);

const USER_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/user/exportXls";
const USER_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/user/importExcel";


//角色
const ROLE_ALL_LIST_SERVICE = (params) => postAction("/sys/sysRole/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_LIST_SERVICE = (params) => postAction("/sys/sysRole/list", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_ADD_SERVICE = (params) => postAction("/sys/sysRole/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_EDIT_SERVICE = (params) => putAction("/sys/sysRole/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_QUERY_SERVICE = (params) => getAction("/sys/sysRole/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_SERVICE = (params) => deleteAction("/sys/sysRole/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysRole/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_USER_LIST_SERVICE = (params) => getAction("/sys/user/userRoleList", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_ADD_USER_SERVICE = (params) => postAction("/sys/user/addSysUserRole", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_DELETE_USER_SERVICE = (params) => deleteAction("/sys/user/deleteUserRole", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_MENU_LIST_SERVICE = (params) => getAction("/sys/permission/queryRolePermission", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_MENU_ALL_LIST_SERVICE = (params) => getAction("/sys/sysRole/queryTreeList", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_AUTH_MENU_ADD_SERVICE = (params) => postAction("/sys/permission/saveRolePermission", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysRole/exportXls";
const ROLE_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysRole/importExcel";



//菜单
const MENU_LIST_SERVICE = (params) => postAction("/sys/sysMenu/list", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_ALL_LIST_SERVICE = (params) => postAction("/sys/sysMenu/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_QUERY_SERVICE = (params) => getAction("/sys/sysMenu/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_ADD_SERVICE = (params) => postAction("/sys/sysMenu/add", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_EDIT_SERVICE = (params) => putAction("/sys/sysMenu/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_DELETE_SERVICE = (params) => deleteAction("/sys/sysMenu/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysMenu/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_USER_LIST_SERVICE = (params) => getAction("/sys/sysMenu/getMenuListByTocken", appSettings.jeecg.serviceSiteRootUrl, params);


//职务
const POSITION_LIST_SERVICE = (params) => getAction("/sys/position/list", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_ADD_SERVICE = (params) => postAction("/sys/position/add", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_EDIT_SERVICE = (params) => putAction("/sys/position/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_QUERY_SERVICE = (params) => getAction("/sys/position/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_DELETE_SERVICE = (params) => deleteAction("/sys/position/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/position/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const POSITION_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/position/exportXls";
const POSITION_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/position/importExcel";


//字典
const DICT_TYPT_LIST_SERVICE = (params) => postAction("/sys/sysDictType/list", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_ADD_SERVICE = (params) => postAction("/sys/sysDictType/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_EDIT_SERVICE = (params) => putAction("/sys/sysDictType/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_QUERY_SERVICE = (params) => getAction("/sys/sysDictType/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_DELETE_SERVICE = (params) => deleteAction("/sys/sysDictType/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysDictType/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysDictType/exportXls";
const DICT_TYPT_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysDictType/importExcel";

const DICT_LIST_SERVICE = (params) => postAction("/sys/sysDict/list", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_ALL_LIST_SERVICE = (params) => getAction("/sys/sysDict/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_ADD_SERVICE = (params) => postAction("/sys/sysDict/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_EDIT_SERVICE = (params) => putAction("/sys/sysDict/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_QUERY_SERVICE = (params) => getAction("/sys/sysDict/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_DELETE_SERVICE = (params) => deleteAction("/sys/sysDict/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysDict/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_LIST_BY_DICT_TYPE_CODE_SERVICE = (params) => getAction("/sys/sysDict/getDictByDictTypeCode", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_EXPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysDict/exportXls";
const DICT_IMPORT_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/sysDict/importExcel";


//职务
const POSITION_ALL_LIST_SERVICE = (params) => getAction("/sys/position/queryall", appSettings.jeecg.serviceSiteRootUrl, params);

//部门
const DEPART_TREE_LIST_SERVICE = (params) => getAction("/sys/sysDepart/queryTreeList", appSettings.jeecg.serviceSiteRootUrl, params);


//重复校验
const DUPLICATE_CHECK_SERVICE = (params) => getAction("/sys/duplicate/check", appSettings.jeecg.serviceSiteRootUrl, params);

//文件上传
const FILE_UPLOAD_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/upload";
const FILE_VIEW_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/view/";

export {

    LOGIN_SERVICE,
    LOGIN_OUT_SERVICE,
    CHECK_CODE_SERVICE,
    MENU_USER_LIST_SERVICE,

    USER_LIST_SERVICE,
    USER_ALL_LIST_SERVICE,
    USER_ADD_SERVICE,
    USER_EDIT_SERVICE,
    USER_ROLE_ALL_LIST_SERVICE,
    USER_QUERY_SERVICE,
    USER_DEPART_LIST_SERVICE,
    USER_ONLY_SERVICE,
    USER_CHANGE_PASSWORD_SERVICE,
    USER_BATCH_SERVICE,
    USER_DELETE_SERVICE,
    USER_DELETE_BATCH_SERVICE,
    USER_EXPORT_SERVICE_URL,
    USER_IMPORT_SERVICE_URL,

    ROLE_ALL_LIST_SERVICE,
    ROLE_LIST_SERVICE,
    ROLE_ADD_SERVICE,
    ROLE_QUERY_SERVICE,
    ROLE_EDIT_SERVICE,
    ROLE_DELETE_SERVICE,
    ROLE_DELETE_BATCH_SERVICE,
    ROLE_AUTH_USER_LIST_SERVICE,
    ROLE_AUTH_ADD_USER_SERVICE,
    ROLE_AUTH_DELETE_USER_SERVICE,
    ROLE_EXPORT_SERVICE_URL,
    ROLE_IMPORT_SERVICE_URL,
    ROLE_AUTH_MENU_LIST_SERVICE,
    ROLE_AUTH_MENU_ALL_LIST_SERVICE,
    ROLE_AUTH_MENU_ADD_SERVICE,


    MENU_LIST_SERVICE,
    MENU_QUERY_SERVICE,
    MENU_ADD_SERVICE,
    MENU_EDIT_SERVICE,
    MENU_DELETE_SERVICE,
    MENU_DELETE_BATCH_SERVICE,
    MENU_ALL_LIST_SERVICE,

    POSITION_LIST_SERVICE,
    POSITION_ADD_SERVICE,
    POSITION_EDIT_SERVICE,
    POSITION_QUERY_SERVICE,
    POSITION_DELETE_SERVICE,
    POSITION_DELETE_BATCH_SERVICE,
    POSITION_EXPORT_SERVICE_URL,
    POSITION_IMPORT_SERVICE_URL,

    DICT_TYPT_LIST_SERVICE,
    DICT_TYPT_ADD_SERVICE,
    DICT_TYPT_EDIT_SERVICE,
    DICT_TYPT_QUERY_SERVICE,
    DICT_TYPT_DELETE_SERVICE,
    DICT_TYPT_DELETE_BATCH_SERVICE,
    DICT_TYPT_EXPORT_SERVICE_URL,
    DICT_TYPT_IMPORT_SERVICE_URL,

    DICT_LIST_SERVICE,
    DICT_ALL_LIST_SERVICE,
    DICT_ADD_SERVICE,
    DICT_EDIT_SERVICE,
    DICT_QUERY_SERVICE,
    DICT_DELETE_SERVICE,
    DICT_DELETE_BATCH_SERVICE,
    DICT_EXPORT_SERVICE_URL,
    DICT_IMPORT_SERVICE_URL,
    DICT_LIST_BY_DICT_TYPE_CODE_SERVICE,


    DEPART_TREE_LIST_SERVICE,
    POSITION_ALL_LIST_SERVICE,
    DUPLICATE_CHECK_SERVICE,

    FILE_UPLOAD_SERVICE_URL,
    FILE_VIEW_SERVICE_URL
};