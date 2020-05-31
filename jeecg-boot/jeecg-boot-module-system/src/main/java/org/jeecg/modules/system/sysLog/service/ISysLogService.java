package org.jeecg.modules.system.sysLog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecg.modules.system.sysLog.entity.SysLog;

import com.baomidou.mybatisplus.extension.service.IService;


public interface ISysLogService extends IService<SysLog> {

	/**
	 * @功能：清空所有日志记录
	 */
	public void removeAll();
	
	/**
	 * 获取系统总访问次数
	 *
	 * @return Long
	 */
	Long findTotalVisitCount();


	/**
	 * 获取系统今日访问次数
	 *
	 * @return Long
	 */
	Long findTodayVisitCount(Date dayStart, Date dayEnd);

	/**
	 * 获取系统今日访问 IP数
	 *
	 * @return Long
	 */
	Long findTodayIp(Date dayStart, Date dayEnd);

	
	/**
	 *   首页：根据时间统计访问数量/ip数量
	 * @param dayStart
	 * @param dayEnd
	 * @return
	 */
	List<Map<String,Object>> findVisitCount(Date dayStart, Date dayEnd);
}
