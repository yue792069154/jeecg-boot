package org.jeecg.common.system.api;

import java.sql.SQLException;
import java.util.List;

import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20 
 * @Version:V1.0
 */
public interface ISysBaseAPI {

	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param logType 日志类型(0:操作日志;1:登录日志;2:定时任务)
	 * @param operatetype 操作类型(1:添加;2:修改;3:删除;)
	 */
	void addLog(String LogContent, Integer logType, Integer operatetype);
	
	/**
	  * 根据用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public LoginUser getUserByName(String username);

	/**
	  * 根据用户id查询用户信息
	 * @param id
	 * @return
	 */
	public LoginUser getUserById(String id);


	/**
	 * 获取当前数据库类型
	 * @return
	 * @throws Exception 
	 */
	public String getDatabaseType() throws SQLException;


	/**
	 * 获取所有有效用户
	 * @return
	 */
	public List<ComboModel> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     * @return
     */
    public List<ComboModel> queryAllUser(String[] userIds);


}
