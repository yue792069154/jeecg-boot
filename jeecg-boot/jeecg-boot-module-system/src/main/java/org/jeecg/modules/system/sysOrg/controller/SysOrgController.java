package org.jeecg.modules.system.sysOrg.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysOrg.entity.SysOrg;
import org.jeecg.modules.system.sysOrg.service.ISysOrgService;
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
 * @Description: 组织机构
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="组织机构")
@RestController
@RequestMapping("/sysOrg/sysOrg")
public class SysOrgController extends JeecgController<SysOrg, ISysOrgService> {
	@Autowired
	private ISysOrgService sysOrgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysOrg
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "组织机构-分页列表查询")
	@ApiOperation(value="组织机构-分页列表查询", notes="组织机构-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysOrg sysOrg,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("status_code",1)
				.and(wrapper -> wrapper.like("org_name",sysOrg.getOrgName()))
				.orderByAsc("sort");
		Page<SysOrg> page = new Page<SysOrg>(pageNo, pageSize);
		IPage<SysOrg> pageList = sysOrgService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysOrg
	 * @return
	 */
	@AutoLog(value = "组织机构-添加")
	@ApiOperation(value="组织机构-添加", notes="组织机构-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysOrg sysOrg) {
		sysOrgService.save(sysOrg);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysOrg
	 * @return
	 */
	@AutoLog(value = "组织机构-编辑")
	@ApiOperation(value="组织机构-编辑", notes="组织机构-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysOrg sysOrg) {
		sysOrgService.updateById(sysOrg);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组织机构-通过id删除")
	@ApiOperation(value="组织机构-通过id删除", notes="组织机构-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysOrgService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "组织机构-批量删除")
	@ApiOperation(value="组织机构-批量删除", notes="组织机构-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysOrgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "组织机构-通过id查询")
	@ApiOperation(value="组织机构-通过id查询", notes="组织机构-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysOrg sysOrg = sysOrgService.getById(id);
		return Result.ok(sysOrg);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysOrg
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysOrg sysOrg) {
      return super.exportXls(request, sysOrg, SysOrg.class, "组织机构");
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
      return super.importExcel(request, response, SysOrg.class);
  }

}
