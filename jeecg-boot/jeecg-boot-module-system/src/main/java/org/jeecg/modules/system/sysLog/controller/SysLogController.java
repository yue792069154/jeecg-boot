package org.jeecg.modules.system.sysLog.controller;


import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysLog.entity.SysLog;
import org.jeecg.modules.system.sysLog.service.ISysLogService;
import org.jeecg.modules.system.sysRole.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {
	
	@Autowired
	private ISysLogService sysLogService;
	
	/**
	 * @功能：查询日志记录
	 * @param syslog
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
											   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
		String keyword=httpServletRequest.getParameter("keyword");
		queryWrapper.like(!StringUtil.isNullOrEmpty(keyword),"log_content",keyword);
		Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
		IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
		return Result.ok(pageList);

	}
	
	/**
	 * @功能：删除单个日志记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysLog> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysLog> result = new Result<SysLog>();
		SysLog sysLog = sysLogService.getById(id);
		if(sysLog==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysLogService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}
	
	/**
	 * @功能：批量，全部清空日志记录
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysRole> result = new Result<SysRole>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			if("allclear".equals(ids)) {
				this.sysLogService.removeAll();
				result.success("清除成功!");
			}
			this.sysLogService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	
}
