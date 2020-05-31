package org.jeecg.modules.system.dict.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysDict.entity.SysDictType;
import org.jeecg.modules.system.sysDict.service.ISysDictTypeService;
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
 * @Description: 字典类型
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="字典类型")
@RestController
@RequestMapping("/sys/sysDictType")
public class SysDictTypeController extends JeecgController<SysDictType, ISysDictTypeService> {
	@Autowired
	private ISysDictTypeService sysDictTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysDictType
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "字典类型-分页列表查询")
	@ApiOperation(value="字典类型-分页列表查询", notes="字典类型-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody  SysDictType sysDictType ,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("status_code",1)
				.and(wrapper -> wrapper.like("dict_type_name",sysDictType.getDictTypeName()).or().like("dict_type_code",sysDictType.getDictTypeCode()))
		.orderByAsc("sort");
		Page<SysDictType> page = new Page<SysDictType>(pageNo, pageSize);
		IPage<SysDictType> pageList = sysDictTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysDictType
	 * @return
	 */
	@AutoLog(value = "字典类型-添加")
	@ApiOperation(value="字典类型-添加", notes="字典类型-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysDictType sysDictType) {
		sysDictTypeService.save(sysDictType);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysDictType
	 * @return
	 */
	@AutoLog(value = "字典类型-编辑")
	@ApiOperation(value="字典类型-编辑", notes="字典类型-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysDictType sysDictType) {
		sysDictTypeService.updateById(sysDictType);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典类型-通过id删除")
	@ApiOperation(value="字典类型-通过id删除", notes="字典类型-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysDictTypeService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "字典类型-批量删除")
	@ApiOperation(value="字典类型-批量删除", notes="字典类型-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysDictTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典类型-通过id查询")
	@ApiOperation(value="字典类型-通过id查询", notes="字典类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysDictType sysDictType = sysDictTypeService.getById(id);
		return Result.ok(sysDictType);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysDictType
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysDictType sysDictType) {
      return super.exportXls(request, sysDictType, SysDictType.class, "字典类型");
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
      return super.importExcel(request, response, SysDictType.class);
  }

}
