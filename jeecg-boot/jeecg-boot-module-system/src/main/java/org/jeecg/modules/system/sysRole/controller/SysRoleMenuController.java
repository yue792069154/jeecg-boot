package org.jeecg.modules.system.sysRole.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;

import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysRole.entity.SysRoleMenu;
import org.jeecg.modules.system.sysRole.service.ISysRoleMenuService;
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
 * @Description: 角色菜单
 * @Author: LE WEN
 * @Date:   2020-05-31
 * @Version: V1.0
 */
@Slf4j
@Api(tags="角色菜单")
@RestController
@RequestMapping("/sys/sysRoleMenu")
public class SysRoleMenuController extends JeecgController<SysRoleMenu, ISysRoleMenuService> {
	@Autowired
	private ISysRoleMenuService sysRoleMenuService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysRoleMenu
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "角色菜单-分页列表查询")
	@ApiOperation(value="角色菜单-分页列表查询", notes="角色菜单-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysRoleMenu sysRoleMenu,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
		Page<SysRoleMenu> page = new Page<SysRoleMenu>(pageNo, pageSize);
		IPage<SysRoleMenu> pageList = sysRoleMenuService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 列表查询
	  *
	  * @param sysRoleMenu
	  * @return
	  */
	 @AutoLog(value = "角色菜单-列表查询")
	 @ApiOperation(value="角色菜单-列表查询", notes="角色菜单-列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody SysRoleMenu sysRoleMenu) {
		 QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq(!StringUtil.isNullOrEmpty(sysRoleMenu.getRoleId()),"role_id",sysRoleMenu.getRoleId());
		 List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.list(queryWrapper);
		 return Result.ok(sysRoleMenuList);
	 }
	
	/**
	 * 添加
	 *
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@AutoLog(value = "角色菜单-添加")
	@ApiOperation(value="角色菜单-添加", notes="角色菜单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysRoleMenu sysRoleMenu) {

		Result<?> result = new Result<>();
		String [] menuIdList = sysRoleMenu.getMenuIds().split(",");
		try {

			QueryWrapper queryWrapper=new QueryWrapper();
			queryWrapper.eq(!StringUtil.isNullOrEmpty(sysRoleMenu.getRoleId()),"role_id",sysRoleMenu.getRoleId());
			sysRoleMenuService.remove(queryWrapper);

			for (String menuId : menuIdList) {
				this.sysRoleMenuService.save(new SysRoleMenu().setRoleId(sysRoleMenu.getRoleId()).setMenuId(menuId));
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败"+e.getMessage());
		}

		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysRoleMenu
	 * @return
	 */
	@AutoLog(value = "角色菜单-编辑")
	@ApiOperation(value="角色菜单-编辑", notes="角色菜单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysRoleMenu sysRoleMenu) {
		sysRoleMenuService.updateById(sysRoleMenu);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色菜单-通过id删除")
	@ApiOperation(value="角色菜单-通过id删除", notes="角色菜单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysRoleMenuService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "角色菜单-批量删除")
	@ApiOperation(value="角色菜单-批量删除", notes="角色菜单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysRoleMenuService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "角色菜单-通过id查询")
	@ApiOperation(value="角色菜单-通过id查询", notes="角色菜单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysRoleMenu sysRoleMenu = sysRoleMenuService.getById(id);
		return Result.ok(sysRoleMenu);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysRoleMenu
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysRoleMenu sysRoleMenu) {
      return super.exportXls(request, sysRoleMenu, SysRoleMenu.class, "角色菜单");
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
      return super.importExcel(request, response, SysRoleMenu.class);
  }

}
