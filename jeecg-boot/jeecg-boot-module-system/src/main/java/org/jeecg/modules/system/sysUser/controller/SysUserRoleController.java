package org.jeecg.modules.system.sysUser.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysUser.entity.SysUserRole;
import org.jeecg.modules.system.sysUser.service.ISysUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 用户角色
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户角色")
@RestController
@RequestMapping("/sys/sysUserRole")
public class SysUserRoleController extends JeecgController<SysUserRole, ISysUserRoleService> {
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysUserRole
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "用户角色-分页列表查询")
	@ApiOperation(value="用户角色-分页列表查询", notes="用户角色-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysUserRole sysUserRole,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
		Page<SysUserRole> page = new Page<SysUserRole>(pageNo, pageSize);
		IPage<SysUserRole> pageList = sysUserRoleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 列表查询
	  *
	  * @param sysUserRole
	  * @return
	  */
	 @AutoLog(value = "用户角色-列表查询")
	 @ApiOperation(value="用户角色-列表查询", notes="用户角色-列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody SysUserRole sysUserRole) {
		 QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq(!StringUtil.isNullOrEmpty(sysUserRole.getUserId()),"user_id",sysUserRole.getUserId());
		 List<SysUserRole> sysUserRoleList = sysUserRoleService.list(queryWrapper);
		 return Result.ok(sysUserRoleList);
	 }
	
	/**
	 * 添加
	 *
	 * @param sysUserRole
	 * @return
	 */
	@AutoLog(value = "用户角色-添加")
	@ApiOperation(value="用户角色-添加", notes="用户角色-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysUserRole sysUserRole) {
		sysUserRoleService.save(sysUserRole);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sysUserRole
	 * @return
	 */
	@AutoLog(value = "用户角色-编辑")
	@ApiOperation(value="用户角色-编辑", notes="用户角色-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysUserRole sysUserRole) {
		sysUserRoleService.updateById(sysUserRole);
		return Result.ok("编辑成功!");
	}

	 /**
	  * 通过id删除
	  *
	  * @param userId
	  * @param roleId
	  * @return
	  */
	 @AutoLog(value = "用户角色-通过userId、roleId删除")
	 @ApiOperation(value="用户角色-通过userId、roleId删除", notes="用户角色-通过userId、roleId删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="userId",required=true) String userId,@RequestParam(name="roleId",required=true) String roleId) {
		 QueryWrapper<SysUserRole> queryWrapper =new QueryWrapper<>();
		 queryWrapper.eq("user_id",userId).eq("role_id",roleId);
		 sysUserRoleService.remove(queryWrapper);
		 return Result.ok("删除成功!");
	 }
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户角色-批量删除")
	@ApiOperation(value="用户角色-批量删除", notes="用户角色-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUserRoleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户角色-通过id查询")
	@ApiOperation(value="用户角色-通过id查询", notes="用户角色-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysUserRole sysUserRole = sysUserRoleService.getById(id);
		return Result.ok(sysUserRole);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysUserRole
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysUserRole sysUserRole) {
      return super.exportXls(request, sysUserRole, SysUserRole.class, "用户角色");
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
      return super.importExcel(request, response, SysUserRole.class);
  }

}
