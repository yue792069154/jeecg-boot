package org.jeecg.common.constant;

public interface CommonConstant {

	/**
	 * 正常状态
	 */
	public static final Integer STATUS_NORMAL = 0;

	/**
	 * 禁用状态
	 */
	public static final Integer STATUS_DISABLE = -1;

	/**
	 * 删除标志
	 */
	public static final Integer DEL_FLAG_1 = 1;

	/**
	 * 未删除
	 */
	public static final Integer DEL_FLAG_0 = 0;

	/**
	 * 系统日志类型： 登录
	 */
	public static final int LOG_TYPE_1 = 1;
	
	/**
	 * 系统日志类型： 操作
	 */
	public static final int LOG_TYPE_2 = 2;

	/**
	 * 操作日志类型： 查询
	 */
	public static final int OPERATE_TYPE_1 = 1;
	
	/**
	 * 操作日志类型： 添加
	 */
	public static final int OPERATE_TYPE_2 = 2;
	
	/**
	 * 操作日志类型： 更新
	 */
	public static final int OPERATE_TYPE_3 = 3;
	
	/**
	 * 操作日志类型： 删除
	 */
	public static final int OPERATE_TYPE_4 = 4;
	
	/**
	 * 操作日志类型： 倒入
	 */
	public static final int OPERATE_TYPE_5 = 5;
	
	/**
	 * 操作日志类型： 导出
	 */
	public static final int OPERATE_TYPE_6 = 6;
	
	
	/** {@code 500 Server Error} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /** {@code 200 OK} (HTTP/1.0 - RFC 1945) */
    public static final Integer SC_OK_200 = 200;
    
    /**访问权限认证未通过 510*/
    public static final Integer SC_JEECG_NO_AUTHZ=510;

    /** 登录用户Shiro权限缓存KEY前缀 */
    public static String PREFIX_USER_SHIRO_CACHE  = "shiro:cache:org.jeecg.modules.shiro.authc.ShiroRealm.authorizationCache:";
    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";
    /** Token缓存时间：3600秒即一小时 */
    public static final int  TOKEN_EXPIRE_TIME  = 3600;



    /**
     * 状态(0无效1有效)
     */
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";


    /**
     * 是否用户已被冻结 1(解冻)正常 2冻结
     */
    public static final Integer USER_UNFREEZE = 1;
    public static final Integer USER_FREEZE = 2;


}
