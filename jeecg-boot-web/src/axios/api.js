import {
    getAction,
    deleteAction,
    putAction,
    postAction
} from '../libs/fw-core/fw-request/action';

import appSettings from "../common/appSettings";

//登陆、注销
const LOGIN_SERVICE = (params) => postAction("/sys/login", appSettings.jeecg.serviceSiteRootUrl, params);
const LOGIN_OUT_SERVICE = (params) => postAction("/sys/logout", appSettings.jeecg.serviceSiteRootUrl, params);

//获取验证码
const CHECK_CODE_SERVICE = (params) => getAction("/sys/getCheckCode", appSettings.jeecg.serviceSiteRootUrl, params);


//用户
const USER_LIST_SERVICE = (params) => getAction("/sys/sysUser/list", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ALL_LIST_SERVICE = (params) => postAction("/sys/sysUser/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ADD_SERVICE = (params) => postAction("/sys/sysUser/add", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_EDIT_SERVICE = (params) => putAction("/sys/sysUser/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_QUERY_SERVICE = (params) => getAction("/sys/sysUser/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_SERVICE = (params) => deleteAction("/sys/sysUser/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysUser/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_BATCH_SERVICE = (params) => getAction("/sys/sysUser/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_CHANGE_PASSWORD_SERVICE = (params) => putAction("/sys/sysUser/changPassword", appSettings.jeecg.serviceSiteRootUrl, params);
const USER_ROLE_ALL_LIST_SERVICE = (params) => postAction("/sys/sysUserRole/queryall", appSettings.jeecg.serviceSiteRootUrl, params);


//角色
const ROLE_ALL_LIST_SERVICE = (params) => postAction("/sys/sysRole/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_LIST_SERVICE = (params) => getAction("/sys/sysRole/list", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_ADD_SERVICE = (params) => postAction("/sys/sysRole/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_EDIT_SERVICE = (params) => putAction("/sys/sysRole/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_QUERY_SERVICE = (params) => getAction("/sys/sysRole/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_SERVICE = (params) => deleteAction("/sys/sysRole/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysRole/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);

//角色绑定菜单
const ROLE_MENU_LIST_SERVICE = (params) => postAction("/sys/sysRoleMenu/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_MENU_ADD_SERVICE = (params) => postAction("/sys/sysRoleMenu/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_MENU_DELETE_SERVICE = (params) => postAction("/sys/sysRoleMenu/delete", appSettings.jeecg.serviceSiteRootUrl, params);

//角色绑定用户
const ROLE_ADD_USER_SERVICE = (params) => postAction("/sys/sysUserRole/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_USER_SERVICE = (params) => deleteAction("/sys/sysUserRole/delete", appSettings.jeecg.serviceSiteRootUrl, params);


//菜单
const MENU_LIST_SERVICE = (params) => getAction("/sys/sysMenu/list", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_ALL_LIST_SERVICE = (params) => postAction("/sys/sysMenu/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_QUERY_SERVICE = (params) => getAction("/sys/sysMenu/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_ADD_SERVICE = (params) => postAction("/sys/sysMenu/add", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_EDIT_SERVICE = (params) => putAction("/sys/sysMenu/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_DELETE_SERVICE = (params) => deleteAction("/sys/sysMenu/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysMenu/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_USER_LIST_SERVICE = (params) => getAction("/sys/sysMenu/getMenuListByTocken", appSettings.jeecg.serviceSiteRootUrl, params);
const MENU_BATCH_SERVICE = (params) => getAction("/sys/sysMenu/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);

//字典
const DICT_TYPT_LIST_SERVICE = (params) => getAction("/sys/sysDictType/list", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_ADD_SERVICE = (params) => postAction("/sys/sysDictType/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_EDIT_SERVICE = (params) => putAction("/sys/sysDictType/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_QUERY_SERVICE = (params) => getAction("/sys/sysDictType/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_DELETE_SERVICE = (params) => deleteAction("/sys/sysDictType/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysDictType/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_TYPT_BATCH_SERVICE = (params) => getAction("/sys/sysDictType/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);

const DICT_LIST_SERVICE = (params) => getAction("/sys/sysDict/list", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_ALL_LIST_SERVICE = (params) => getAction("/sys/sysDict/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_ADD_SERVICE = (params) => postAction("/sys/sysDict/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_EDIT_SERVICE = (params) => putAction("/sys/sysDict/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_QUERY_SERVICE = (params) => getAction("/sys/sysDict/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_DELETE_SERVICE = (params) => deleteAction("/sys/sysDict/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysDict/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_LIST_BY_DICT_TYPE_CODE_SERVICE = (params) => getAction("/sys/sysDict/getDictByDictTypeCode", appSettings.jeecg.serviceSiteRootUrl, params);
const DICT_BATCH_SERVICE = (params) => getAction("/sys/sysDict/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);

//日志
const LOG_LIST_SERVICE = (params) => getAction("/sys/log/list", appSettings.jeecg.serviceSiteRootUrl, params);

//文件上传
const FILE_UPLOAD_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/upload";
const FILE_VIEW_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/view/";

export {

    //登陆、注销
    LOGIN_SERVICE,
    LOGIN_OUT_SERVICE,

    //获取验证码
    CHECK_CODE_SERVICE,


    //用户
    USER_LIST_SERVICE,
    USER_ALL_LIST_SERVICE,
    USER_ADD_SERVICE,
    USER_EDIT_SERVICE,
    USER_QUERY_SERVICE,
    USER_DELETE_SERVICE,
    USER_DELETE_BATCH_SERVICE,
    USER_BATCH_SERVICE,
    USER_CHANGE_PASSWORD_SERVICE,
    USER_ROLE_ALL_LIST_SERVICE,


    //角色
    ROLE_ALL_LIST_SERVICE,
    ROLE_LIST_SERVICE,
    ROLE_ADD_SERVICE,
    ROLE_EDIT_SERVICE,
    ROLE_QUERY_SERVICE,
    ROLE_DELETE_SERVICE,
    ROLE_DELETE_BATCH_SERVICE,

    //角色绑定菜单
    ROLE_MENU_LIST_SERVICE,
    ROLE_MENU_ADD_SERVICE,
    ROLE_MENU_DELETE_SERVICE,

    //角色绑定用户
    ROLE_ADD_USER_SERVICE,
    ROLE_DELETE_USER_SERVICE,


    //菜单
    MENU_LIST_SERVICE,
    MENU_ALL_LIST_SERVICE,
    MENU_QUERY_SERVICE,
    MENU_ADD_SERVICE,
    MENU_EDIT_SERVICE,
    MENU_DELETE_SERVICE,
    MENU_DELETE_BATCH_SERVICE,
    MENU_USER_LIST_SERVICE,
    MENU_BATCH_SERVICE,

    //字典
    DICT_TYPT_LIST_SERVICE,
    DICT_TYPT_ADD_SERVICE,
    DICT_TYPT_EDIT_SERVICE,
    DICT_TYPT_QUERY_SERVICE,
    DICT_TYPT_DELETE_SERVICE,
    DICT_TYPT_DELETE_BATCH_SERVICE,
    DICT_TYPT_BATCH_SERVICE,

    DICT_LIST_SERVICE,
    DICT_ALL_LIST_SERVICE,
    DICT_ADD_SERVICE,
    DICT_EDIT_SERVICE,
    DICT_QUERY_SERVICE,
    DICT_DELETE_SERVICE,
    DICT_DELETE_BATCH_SERVICE,
    DICT_LIST_BY_DICT_TYPE_CODE_SERVICE,
    DICT_BATCH_SERVICE,

    //日志
    LOG_LIST_SERVICE,


    //文件上传
    FILE_UPLOAD_SERVICE_URL,
    FILE_VIEW_SERVICE_URL
};