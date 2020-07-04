package org.jeecg.modules.system.sysAuth.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysAuth.entity.SysAuth;
import org.jeecg.modules.system.sysAuth.service.ISysAuthService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 功能管理
 * @Author: LE WEN
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="功能管理")
@RestController
@RequestMapping("/sys/sysAuth")
public class SysAuthController extends JeecgController<SysAuth, ISysAuthService> {
	@Autowired
	private ISysAuthService sysAuthService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "功能管理-分页列表查询")
	@ApiOperation(value="功能管理-分页列表查询", notes="功能管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");

		QueryWrapper<SysAuth> queryWrapper = new QueryWrapper<>();
		queryWrapper
				.like(!StringUtil.isNullOrEmpty(keyword),"auth_name",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"auth_code",keyword)
				.orderByAsc("auth_name","auth_code");
		Page<SysAuth> page = new Page<SysAuth>(pageNo, pageSize);
		IPage<SysAuth> pageList = sysAuthService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 列表查询
	  *
	  * @param sysAuth
	  * @return
	  */
	 @AutoLog(value = "功能-列表查询")
	 @ApiOperation(value="功能-列表查询", notes="功能-列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody SysAuth sysAuth) {
		 return Result.ok(sysAuthService.queryList(sysAuth));
	 }


	 /**
	  * 用户的全部列表查询
	  *
	  * @param token
	  * @return
	  */
	 @GetMapping(value = "/getAuthListByTocken")
	 public Result<?> getAuthListByTocken(@RequestParam(name = "token", required = true) String token){

		 Result<?> result = new Result<>();
		 try {
			 if (oConvertUtils.isEmpty(token)) {
				 return Result.error("TOKEN不允许为空！");
			 }

			 String userName = JwtUtil.getUserName(token);
			 List<SysAuth> sysMenuList = sysAuthService.getAuthListByUserName(userName);
			 return Result.ok(sysMenuList);

		 } catch (Exception e) {
			 result.error500("查询失败:" + e.getMessage());
			 log.error(e.getMessage(), e);
		 }
		 return result;
	 }
	
	/**
	 * 添加
	 *
	 * @param sysAuth
	 * @return
	 */
	@AutoLog(value = "功能管理-添加")
	@ApiOperation(value="功能管理-添加", notes="功能管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysAuth sysAuth) {
		sysAuthService.save(sysAuth);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysAuth
	 * @return
	 */
	@AutoLog(value = "功能管理-编辑")
	@ApiOperation(value="功能管理-编辑", notes="功能管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysAuth sysAuth) {
		sysAuthService.updateById(sysAuth);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "功能管理-通过id删除")
	@ApiOperation(value="功能管理-通过id删除", notes="功能管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysAuthService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "功能管理-批量删除")
	@ApiOperation(value="功能管理-批量删除", notes="功能管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAuthService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	 /**
	  * 启用&停用功能
	  * @param ids
	  * @return
	  */
	 @GetMapping(value = "/frozenBatch")
	 public Result<SysAuth> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="statusCode",required=true) String statusCode) {
		 Result<SysAuth> result = new Result<SysAuth>();
		 String [] idList = ids.split(",");
		 try {
			 for (String id : idList) {
				 this.sysAuthService.update(new SysAuth().setStatusCode(statusCode),
						 new UpdateWrapper<SysAuth>().lambda().eq(SysAuth::getId,id));
			 }
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 result.error500("操作失败"+e.getMessage());
		 }
		 result.success("操作成功!");
		 return result;

	 }


	 /**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "功能管理-通过id查询")
	@ApiOperation(value="功能管理-通过id查询", notes="功能管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysAuth sysAuth = sysAuthService.getById(id);
		return Result.ok(sysAuth);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysAuth
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysAuth sysAuth) {
      return super.exportXls(request, sysAuth, SysAuth.class, "功能管理");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, SysAuth.class);
  }

}
