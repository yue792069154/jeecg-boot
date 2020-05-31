package org.jeecg.modules.system.dict.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.system.sysDict.entity.SysDict;
import org.jeecg.modules.system.dict.service.ISysDictService;
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
 * @Description: 字典
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Slf4j
@Api(tags="字典")
@RestController
@RequestMapping("/sys/sysDict")
public class SysDictController extends JeecgController<SysDict, ISysDictService> {
	@Autowired
	private ISysDictService sysDictService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysDict
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "字典-分页列表查询")
	@ApiOperation(value="字典-分页列表查询", notes="字典-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysDict sysDict,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
		queryWrapper.ne("status_code",1)
				.and(wrapper -> wrapper.like("dict_name",sysDict.getDictName()).or().like("dict_code",sysDict.getDictCode()))
				.orderByAsc("sort");
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param sysDict
	 * @return
	 */
	@AutoLog(value = "字典-添加")
	@ApiOperation(value="字典-添加", notes="字典-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysDict sysDict) {
		sysDictService.save(sysDict);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param sysDict
	 * @return
	 */
	@AutoLog(value = "字典-编辑")
	@ApiOperation(value="字典-编辑", notes="字典-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysDict sysDict) {
		sysDictService.updateById(sysDict);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典-通过id删除")
	@ApiOperation(value="字典-通过id删除", notes="字典-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysDictService.removeById(id);
		return Result.ok("删除成功!");
	}

	 /**
	  * 根据dictTypeCode查询字典列表
	  *
	  * @param dictTypeCode
	  * @return
	  */
	 @AutoLog(value = "字典列表查询")
	 @ApiOperation(value="字典列表查询", notes="字典列表查询")
	 @GetMapping(value = "/getDictByDictTypeCode")
	 public Result<?> getDictByDictTypeCode(@RequestParam(name = "dictTypeCode",required = true) String dictTypeCode) {
		 List<SysDict> sysDictList = sysDictService.getDictByDictTypeCode(dictTypeCode);
		 return Result.ok(sysDictList);
	 }
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "字典-批量删除")
	@ApiOperation(value="字典-批量删除", notes="字典-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysDictService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "字典-通过id查询")
	@ApiOperation(value="字典-通过id查询", notes="字典-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysDict sysDict = sysDictService.getById(id);
		return Result.ok(sysDict);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param sysDict
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysDict sysDict) {
      return super.exportXls(request, sysDict, SysDict.class, "字典");
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
      return super.importExcel(request, response, SysDict.class);
  }

}
