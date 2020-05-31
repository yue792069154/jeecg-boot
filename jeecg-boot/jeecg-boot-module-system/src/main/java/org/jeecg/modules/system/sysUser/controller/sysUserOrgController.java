package org.jeecg.modules.system.sysUser.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysUser.entity.sysUserOrg;
import org.jeecg.modules.system.sysUser.service.IsysUserOrgService;
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
 * @Description: 用户机构
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="用户机构")
@RestController
@RequestMapping("/sysUser/sysUserOrg")
public class sysUserOrgController extends JeecgController<sysUserOrg, IsysUserOrgService> {
	@Autowired
	private IsysUserOrgService sysUserOrgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysUserOrg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "用户机构-分页列表查询")
	@ApiOperation(value="用户机构-分页列表查询", notes="用户机构-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(sysUserOrg sysUserOrg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<sysUserOrg> queryWrapper = new QueryWrapper<>();
		Page<sysUserOrg> page = new Page<sysUserOrg>(pageNo, pageSize);
		IPage<sysUserOrg> pageList = sysUserOrgService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysUserOrg
	 * @return
	 */
	@AutoLog(value = "用户机构-添加")
	@ApiOperation(value="用户机构-添加", notes="用户机构-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody sysUserOrg sysUserOrg) {
		sysUserOrgService.save(sysUserOrg);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysUserOrg
	 * @return
	 */
	@AutoLog(value = "用户机构-编辑")
	@ApiOperation(value="用户机构-编辑", notes="用户机构-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody sysUserOrg sysUserOrg) {
		sysUserOrgService.updateById(sysUserOrg);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户机构-通过id删除")
	@ApiOperation(value="用户机构-通过id删除", notes="用户机构-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysUserOrgService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户机构-批量删除")
	@ApiOperation(value="用户机构-批量删除", notes="用户机构-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUserOrgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户机构-通过id查询")
	@ApiOperation(value="用户机构-通过id查询", notes="用户机构-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		sysUserOrg sysUserOrg = sysUserOrgService.getById(id);
		return Result.ok(sysUserOrg);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysUserOrg
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, sysUserOrg sysUserOrg) {
      return super.exportXls(request, sysUserOrg, sysUserOrg.class, "用户机构");
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
      return super.importExcel(request, response, sysUserOrg.class);
  }

}
