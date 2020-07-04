package org.jeecg.modules.system.sysRole.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysRole.entity.SysRoleAuth;
import org.jeecg.modules.system.sysRole.service.ISysRoleAuthService;
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
 * @Description: 角色功能
 * @Author: LE WEN
 * @Date:   2020-07-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色功能")
@RestController
@RequestMapping("/sys/sysRoleAuth")
public class SysRoleAuthController extends JeecgController<SysRoleAuth, ISysRoleAuthService> {
	@Autowired
	private ISysRoleAuthService sysRoleAuthService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "角色功能-分页列表查询")
	@ApiOperation(value="角色功能-分页列表查询", notes="角色功能-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");

		QueryWrapper<SysRoleAuth> queryWrapper = new QueryWrapper<>();

		Page<SysRoleAuth> page = new Page<SysRoleAuth>(pageNo, pageSize);
		IPage<SysRoleAuth> pageList = sysRoleAuthService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysRoleAuth
	 * @return
	 */
	@AutoLog(value = "角色功能-添加")
	@ApiOperation(value="角色功能-添加", notes="角色功能-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysRoleAuth sysRoleAuth) {
		sysRoleAuthService.save(sysRoleAuth);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysRoleAuth
	 * @return
	 */
	@AutoLog(value = "角色功能-编辑")
	@ApiOperation(value="角色功能-编辑", notes="角色功能-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysRoleAuth sysRoleAuth) {
		sysRoleAuthService.updateById(sysRoleAuth);
		return Result.ok("编辑成功!");
	}


	 /**
	  * 通过id删除
	  *
	  * @param authId
	  * @param roleId
	  * @return
	  */
	 @AutoLog(value = "角色功能-通过authId、roleId删除")
	 @ApiOperation(value="角色功能-通过authId、roleId删除", notes="角色功能-通过authId、roleId删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="authId",required=true) String authId,@RequestParam(name="roleId",required=true) String roleId) {
		 QueryWrapper<SysRoleAuth> queryWrapper =new QueryWrapper<>();
		 queryWrapper.eq("auth_id",authId).eq("role_id",roleId);
		 sysRoleAuthService.remove(queryWrapper);
		 return Result.ok("删除成功!");
	 }
	
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "角色功能-批量删除")
	@ApiOperation(value="角色功能-批量删除", notes="角色功能-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysRoleAuthService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色功能-通过id查询")
	@ApiOperation(value="角色功能-通过id查询", notes="角色功能-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysRoleAuth sysRoleAuth = sysRoleAuthService.getById(id);
		return Result.ok(sysRoleAuth);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysRoleAuth
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysRoleAuth sysRoleAuth) {
      return super.exportXls(request, sysRoleAuth, SysRoleAuth.class, "角色功能");
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
      return super.importExcel(request, response, SysRoleAuth.class);
  }

}
