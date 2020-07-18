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

//角色绑定功能
const ROLE_ADD_AUTH_SERVICE = (params) => postAction("/sys/sysRoleAuth/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ROLE_DELETE_AUTH_SERVICE = (params) => deleteAction("/sys/sysRoleAuth/delete", appSettings.jeecg.serviceSiteRootUrl, params);

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


//权限
const AUTH_ALL_LIST_SERVICE = (params) => postAction("/sys/sysAuth/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_LIST_SERVICE = (params) => getAction("/sys/sysAuth/list", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_ADD_SERVICE = (params) => postAction("/sys/sysAuth/add", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_EDIT_SERVICE = (params) => putAction("/sys/sysAuth/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_QUERY_SERVICE = (params) => getAction("/sys/sysAuth/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_DELETE_SERVICE = (params) => deleteAction("/sys/sysAuth/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_DELETE_BATCH_SERVICE = (params) => deleteAction("/sys/sysAuth/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_BATCH_SERVICE = (params) => getAction("/sys/sysAuth/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const AUTH_USER_LIST_SERVICE = (params) => getAction("/sys/sysAuth/getAuthListByTocken", appSettings.jeecg.serviceSiteRootUrl, params);


//日志
const LOG_LIST_SERVICE = (params) => getAction("/sys/log/list", appSettings.jeecg.serviceSiteRootUrl, params);

//文件上传
const FILE_UPLOAD_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/upload";
const FILE_VIEW_SERVICE_URL = appSettings.jeecg.serviceSiteRootUrl + "/sys/common/view/";



//设备中心
const DEVICE_ALL_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDevice/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDevice/list", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_ADD_SERVICE = (params) => postAction("/bus/tBusElectricDevice/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_EDIT_SERVICE = (params) => putAction("/bus/tBusElectricDevice/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_QUERY_SERVICE = (params) => getAction("/bus/tBusElectricDevice/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_DELETE_SERVICE = (params) => deleteAction("/bus/tBusElectricDevice/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_DELETE_BATCH_SERVICE = (params) => deleteAction("/bus/tBusElectricDevice/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_BATCH_SERVICE = (params) => getAction("/bus/tBusElectricDevice/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);

//设备监测项目
const DEVICE_POINT_ALL_LIST_SERVICE = (params) => postAction("/bus/tBusElectricDeviceMonitorPoint/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_POINT_ADD_SERVICE = (params) => postAction("/bus/tBusElectricDeviceMonitorPoint/add", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_POINT_EDIT_SERVICE = (params) => putAction("/bus/tBusElectricDeviceMonitorPoint/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_POINT_QUERY_SERVICE = (params) => getAction("/bus/tBusElectricDeviceMonitorPoint/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_POINT_DELETE_SERVICE = (params) => deleteAction("/bus/tBusElectricDeviceMonitorPoint/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const DEVICE_POINT_DELETE_BATCH_SERVICE = (params) => deleteAction("/bus/tBusElectricDeviceMonitorPoint/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);


//监测项目
const POINT_LIST_SERVICE = (params) => getAction("/bus/tBusElectricMonitorPoint/list", appSettings.jeecg.serviceSiteRootUrl, params);
const POINT_ADD_SERVICE = (params) => postAction("/bus/tBusElectricMonitorPoint/add", appSettings.jeecg.serviceSiteRootUrl, params);
const POINT_EDIT_SERVICE = (params) => putAction("/bus/tBusElectricMonitorPoint/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const POINT_QUERY_SERVICE = (params) => getAction("/bus/tBusElectricMonitorPoint/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const POINT_DELETE_SERVICE = (params) => deleteAction("/bus/tBusElectricMonitorPoint/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const POINT_DELETE_BATCH_SERVICE = (params) => deleteAction("/bus/tBusElectricMonitorPoint/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);

//告警中心
const WARNING_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDeviceWarning/list", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_ADD_SERVICE = (params) => postAction("/bus/tBusElectricDeviceWarning/add", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_EDIT_SERVICE = (params) => putAction("/bus/tBusElectricDeviceWarning/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_QUERY_SERVICE = (params) => getAction("/bus/tBusElectricDeviceWarning/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_DELETE_SERVICE = (params) => deleteAction("/bus/tBusElectricDeviceWarning/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_DELETE_BATCH_SERVICE = (params) => deleteAction("/bus/tBusElectricDeviceWarning/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const WARNING_HANDLE_ALL_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDeviceWarningHandle/queryall", appSettings.jeecg.serviceSiteRootUrl, params);


//机构管理
const ORG_LIST_SERVICE = (params) => getAction("/bus/tBusElectricOrg/list", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_ADD_SERVICE = (params) => postAction("/bus/tBusElectricOrg/add", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_EDIT_SERVICE = (params) => putAction("/bus/tBusElectricOrg/edit", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_QUERY_SERVICE = (params) => getAction("/bus/tBusElectricOrg/queryById", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_DELETE_SERVICE = (params) => deleteAction("/bus/tBusElectricOrg/delete", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_DELETE_BATCH_SERVICE = (params) => deleteAction("/bus/tBusElectricOrg/deleteBatch", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_ALL_LIST_SERVICE = (params) => postAction("/bus/tBusElectricOrg/queryall", appSettings.jeecg.serviceSiteRootUrl, params);
const ORG_BATCH_SERVICE = (params) => getAction("/bus/tBusElectricOrg/frozenBatch", appSettings.jeecg.serviceSiteRootUrl, params);



//监测数据
const MONITOR_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDeviceDatReal/list", appSettings.jeecg.serviceSiteRootUrl, params);
const MONITOR_HIS_LIST_SERVICE = (params) => getAction("/bus/tBusElectricDeviceDatReal/historyList", appSettings.jeecg.serviceSiteRootUrl, params);


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


    //角色绑定功能
    ROLE_ADD_AUTH_SERVICE,
    ROLE_DELETE_AUTH_SERVICE,


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

    //权限
    AUTH_ALL_LIST_SERVICE,
    AUTH_LIST_SERVICE,
    AUTH_ADD_SERVICE,
    AUTH_EDIT_SERVICE,
    AUTH_QUERY_SERVICE,
    AUTH_DELETE_SERVICE,
    AUTH_DELETE_BATCH_SERVICE,
    AUTH_BATCH_SERVICE,
    AUTH_USER_LIST_SERVICE,


    //日志
    LOG_LIST_SERVICE,


    //文件上传
    FILE_UPLOAD_SERVICE_URL,
    FILE_VIEW_SERVICE_URL,




    //业务接口-设备
    DEVICE_ALL_LIST_SERVICE,
    DEVICE_LIST_SERVICE,
    DEVICE_ADD_SERVICE,
    DEVICE_EDIT_SERVICE,
    DEVICE_QUERY_SERVICE,
    DEVICE_DELETE_SERVICE,
    DEVICE_DELETE_BATCH_SERVICE,
    DEVICE_BATCH_SERVICE,


    //业务接口-设备监测项目
    DEVICE_POINT_ALL_LIST_SERVICE,
    DEVICE_POINT_ADD_SERVICE,
    DEVICE_POINT_EDIT_SERVICE,
    DEVICE_POINT_QUERY_SERVICE,
    DEVICE_POINT_DELETE_SERVICE,
    DEVICE_POINT_DELETE_BATCH_SERVICE,


    //业务接口-监测项目
    POINT_LIST_SERVICE,
    POINT_ADD_SERVICE,
    POINT_EDIT_SERVICE,
    POINT_QUERY_SERVICE,
    POINT_DELETE_SERVICE,
    POINT_DELETE_BATCH_SERVICE,

    //业务接口-告警信息
    WARNING_LIST_SERVICE,
    WARNING_ADD_SERVICE,
    WARNING_EDIT_SERVICE,
    WARNING_QUERY_SERVICE,
    WARNING_DELETE_SERVICE,
    WARNING_DELETE_BATCH_SERVICE,
    WARNING_HANDLE_ALL_LIST_SERVICE,



    ORG_LIST_SERVICE,
    ORG_ADD_SERVICE,
    ORG_EDIT_SERVICE,
    ORG_QUERY_SERVICE,
    ORG_DELETE_SERVICE,
    ORG_DELETE_BATCH_SERVICE,
    ORG_ALL_LIST_SERVICE,
    ORG_BATCH_SERVICE,

    MONITOR_LIST_SERVICE,
    MONITOR_HIS_LIST_SERVICE

};