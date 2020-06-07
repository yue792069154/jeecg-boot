package org.jeecg.modules.system.sysMenu.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysMenu.entity.SysMenu;
import org.jeecg.modules.system.sysMenu.service.ISysMenuService;
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

@Slf4j
@Api(tags="菜单")
@RestController
@RequestMapping("/sys/sysMenu")
public class SysMenuController extends JeecgController<SysMenu, ISysMenuService> {
	@Autowired
	private ISysMenuService sysMenuService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysMenu
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "菜单-分页列表查询")
	@ApiOperation(value="菜单-分页列表查询", notes="菜单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		String keyword=httpServletRequest.getParameter("keyword");
		String statusCode=httpServletRequest.getParameter("statusCode");
		QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
		queryWrapper
				.like(!StringUtil.isNullOrEmpty(keyword),"menu_name",keyword)
				.eq(!StringUtil.isNullOrEmpty(statusCode),"status_code",statusCode)
				.orderByAsc("sort");
		Page<SysMenu> page = new Page<SysMenu>(pageNo, pageSize);
		IPage<SysMenu> pageList = sysMenuService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 全部列表查询
	 *
	 * @param sysMenu
	 * @return
	 */
	@AutoLog(value = "菜单-全部列表查询")
	@ApiOperation(value="菜单-全部列表查询", notes="菜单-全部列表查询")
	@PostMapping(value = "/queryall")
	public Result<?> queryList(@RequestBody SysMenu sysMenu) {
		QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
		queryWrapper
				.like(!StringUtil.isNullOrEmpty(sysMenu.getKeyword()),"menu_name",sysMenu.getKeyword())
				.eq(!StringUtil.isNullOrEmpty(sysMenu.getStatusCode()),"status_code",sysMenu.getStatusCode())
				.orderByAsc("sort");
		List<SysMenu> sysMenuList = sysMenuService.list(queryWrapper);
		return Result.ok(sysMenuList);
	}

	/**
	 * 用户的全部列表查询
	 *
	 * @param token
	 * @return
	 */
	@GetMapping(value = "/getMenuListByTocken")
	public Result<?> getMenuListByTocken(@RequestParam(name = "token", required = true) String token){

		Result<?> result = new Result<>();
		try {
			if (oConvertUtils.isEmpty(token)) {
				return Result.error("TOKEN不允许为空！");
			}

			String userName = JwtUtil.getUserName(token);
			List<SysMenu> sysMenuList = sysMenuService.getMenuListByUserName(userName);
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
	 * @param sysMenu
	 * @return
	 */
	@AutoLog(value = "菜单-添加")
	@ApiOperation(value="菜单-添加", notes="菜单-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysMenu sysMenu) {
		sysMenuService.save(sysMenu);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysMenu
	 * @return
	 */
	@AutoLog(value = "菜单-编辑")
	@ApiOperation(value="菜单-编辑", notes="菜单-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysMenu sysMenu) {
		sysMenuService.updateById(sysMenu);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "菜单-通过id删除")
	@ApiOperation(value="菜单-通过id删除", notes="菜单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysMenuService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "菜单-批量删除")
	@ApiOperation(value="菜单-批量删除", notes="菜单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysMenuService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}



	/**
	 * 启用&停用菜单
	 * @param ids
	 * @return
	 */
	@GetMapping(value = "/frozenBatch")
	public Result<SysMenu> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="statusCode",required=true) String statusCode) {
		Result<SysMenu> result = new Result<SysMenu>();
		String [] idList = ids.split(",");
		try {
			for (String id : idList) {
				this.sysMenuService.update(new SysMenu().setStatusCode(statusCode),
						new UpdateWrapper<SysMenu>().lambda().eq(SysMenu::getId,id));
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
	@AutoLog(value = "菜单-通过id查询")
	@ApiOperation(value="菜单-通过id查询", notes="菜单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysMenu sysMenu = sysMenuService.getById(id);
		return Result.ok(sysMenu);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysMenu
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysMenu sysMenu) {
      return super.exportXls(request, sysMenu, SysMenu.class, "菜单");
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
      return super.importExcel(request, response, SysMenu.class);
  }

}
