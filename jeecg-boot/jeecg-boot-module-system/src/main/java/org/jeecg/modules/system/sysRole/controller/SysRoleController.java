package org.jeecg.modules.system.sysRole.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysRole.entity.SysRole;
import org.jeecg.modules.system.sysRole.service.ISysRoleService;
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
 * @Description: 角色
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色")
@RestController
@RequestMapping("/sys/sysRole")
public class SysRoleController extends JeecgController<SysRole, ISysRoleService> {
	@Autowired
	private ISysRoleService sysRoleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysRole
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "角色-分页列表查询")
	@ApiOperation(value="角色-分页列表查询", notes="角色-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysRole sysRole,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("status_code",1)
				.and(wrapper -> wrapper.like("role_name",sysRole.getRoleName()))
				.orderByAsc("sort");
		Page<SysRole> page = new Page<SysRole>(pageNo, pageSize);
		IPage<SysRole> pageList = sysRoleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 /**
	  * 分页列表查询
	  *
	  * @param sysRole
	  * @return
	  */
	 @AutoLog(value = "角色-列表查询")
	 @ApiOperation(value="角色-列表查询", notes="角色-列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody SysRole sysRole) {
		 List<SysRole> sysRoleList = sysRoleService.list();
		 return Result.ok(sysRoleList);
	 }


	 /**
	 * 添加
	 *
	 * @param sysRole
	 * @return
	 */
	@AutoLog(value = "角色-添加")
	@ApiOperation(value="角色-添加", notes="角色-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysRole sysRole) {
		sysRoleService.save(sysRole);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysRole
	 * @return
	 */
	@AutoLog(value = "角色-编辑")
	@ApiOperation(value="角色-编辑", notes="角色-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysRole sysRole) {
		sysRoleService.updateById(sysRole);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色-通过id删除")
	@ApiOperation(value="角色-通过id删除", notes="角色-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysRoleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "角色-批量删除")
	@ApiOperation(value="角色-批量删除", notes="角色-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysRoleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色-通过id查询")
	@ApiOperation(value="角色-通过id查询", notes="角色-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysRole sysRole = sysRoleService.getById(id);
		return Result.ok(sysRole);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysRole
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysRole sysRole) {
      return super.exportXls(request, sysRole, SysRole.class, "角色");
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
      return super.importExcel(request, response, SysRole.class);
  }

}
