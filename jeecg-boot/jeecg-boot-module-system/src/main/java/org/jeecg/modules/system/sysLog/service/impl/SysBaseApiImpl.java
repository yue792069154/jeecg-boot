package org.jeecg.modules.system.sysLog.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysLog.entity.SysLog;
import org.jeecg.modules.system.sysUser.entity.SysUser;
import org.jeecg.modules.system.sysLog.mapper.*;
import org.jeecg.modules.system.sysUser.mapper.SysUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20 
 * @Version:V1.0
 */
@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
	/** 当前系统数据库类型 */
	public static String DB_TYPE = "";
	@Resource
	private SysLogMapper sysLogMapper;
	@Autowired
	private SysUserMapper userMapper;
	
	@Override
	public void addLog(String LogContent, Integer logType, Integer operatetype) {
		SysLog sysLog = new SysLog();
		//注解上的描述,操作日志内容
		sysLog.setLogContent(LogContent);
		sysLog.setLogType(logType);
		sysLog.setOperateType(operatetype);

		//请求的方法名
		//请求的参数

		try {
			//获取request
			HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
			//设置IP地址
			sysLog.setIp(IPUtils.getIpAddr(request));
		} catch (Exception e) {
			sysLog.setIp("127.0.0.1");
		}

		//获取登录用户信息
		SysUser sysUser = (SysUser)SecurityUtils.getSubject().getPrincipal();
		if(sysUser!=null){
			sysLog.setUserId(sysUser.getUserName());
			sysLog.setUserName(sysUser.getRealName());

		}
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogMapper.insert(sysLog);
	}

	@Override
	@Cacheable(cacheNames=CacheConstant.SYS_USERS_CACHE, key="#username")
	public LoginUser getUserByName(String username) {
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByUserName(username);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

	@Override
	public LoginUser getUserById(String id) {
		if(oConvertUtils.isEmpty(id)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.selectById(id);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}



	@Override
	public String getDatabaseType() throws SQLException {
		DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
		return getDatabaseTypeByDataSource(dataSource);
	}


	/**
	 * 获取数据库类型
	 * @param dataSource
	 * @return
	 * @throws SQLException
	 */
	private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
		if("".equals(DB_TYPE)) {
			Connection connection = dataSource.getConnection();
			try {
				DatabaseMetaData md = connection.getMetaData();
				String dbType = md.getDatabaseProductName().toLowerCase();
				if(dbType.indexOf("mysql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
				}else if(dbType.indexOf("oracle")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
				}else if(dbType.indexOf("sqlserver")>=0||dbType.indexOf("sql server")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
				}else if(dbType.indexOf("postgresql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
				}else {
					throw new JeecgBootException("数据库类型:["+dbType+"]不识别!");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}finally {
				connection.close();
			}
		}
		return DB_TYPE;
		
	}


	@Override
	public List<ComboModel> queryAllUser() {
		List<ComboModel> list = new ArrayList<ComboModel>();
		List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status","1").eq("del_flag","0"));
		for(SysUser user : userList){
			ComboModel model = new ComboModel();
			model.setTitle(user.getRealName());
			model.setId(user.getId());
			list.add(model);
		}
		return list;
	}

    @Override
    public List<ComboModel> queryAllUser(String[] userIds) {
        List<ComboModel> list = new ArrayList<ComboModel>();
        List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status","1").eq("del_flag","0"));
        for(SysUser user : userList){
            ComboModel model = new ComboModel();
            model.setUsername(user.getUserName());
            model.setTitle(user.getRealName());
            model.setId(user.getId());
            if(oConvertUtils.isNotEmpty(userIds)){
                for(int i = 0; i<userIds.length;i++){
                    if(userIds[i].equals(user.getId())){
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        return list;
    }



}
