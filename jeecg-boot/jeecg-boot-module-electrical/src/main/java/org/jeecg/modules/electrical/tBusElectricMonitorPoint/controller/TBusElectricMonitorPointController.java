package org.jeecg.modules.electrical.tBusElectricMonitorPoint.controller;

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
import org.jeecg.modules.electrical.tBusElectricMonitorPoint.entity.TBusElectricMonitorPoint;
import org.jeecg.modules.electrical.tBusElectricMonitorPoint.service.ITBusElectricMonitorPointService;
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
 * @Description: 监测项目
 * @Author: LE WEN
 * @Date:   2020-07-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="监测项目")
@RestController
@RequestMapping("/bus/tBusElectricMonitorPoint")
public class TBusElectricMonitorPointController extends JeecgController<TBusElectricMonitorPoint, ITBusElectricMonitorPointService> {
	@Autowired
	private ITBusElectricMonitorPointService tBusElectricMonitorPointService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "监测项目-分页列表查询")
	@ApiOperation(value="监测项目-分页列表查询", notes="监测项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");

		QueryWrapper<TBusElectricMonitorPoint> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(!StringUtil.isNullOrEmpty(keyword),"point_name",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"point_code",keyword)
				.orderByAsc("sort");

		Page<TBusElectricMonitorPoint> page = new Page<TBusElectricMonitorPoint>(pageNo, pageSize);
		IPage<TBusElectricMonitorPoint> pageList = tBusElectricMonitorPointService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param tBusElectricMonitorPoint
	 * @return
	 */
	@AutoLog(value = "监测项目-添加")
	@ApiOperation(value="监测项目-添加", notes="监测项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricMonitorPoint tBusElectricMonitorPoint) {
		tBusElectricMonitorPointService.save(tBusElectricMonitorPoint);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricMonitorPoint
	 * @return
	 */
	@AutoLog(value = "监测项目-编辑")
	@ApiOperation(value="监测项目-编辑", notes="监测项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricMonitorPoint tBusElectricMonitorPoint) {
		tBusElectricMonitorPointService.updateById(tBusElectricMonitorPoint);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测项目-通过id删除")
	@ApiOperation(value="监测项目-通过id删除", notes="监测项目-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tBusElectricMonitorPointService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "监测项目-批量删除")
	@ApiOperation(value="监测项目-批量删除", notes="监测项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricMonitorPointService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "监测项目-通过id查询")
	@ApiOperation(value="监测项目-通过id查询", notes="监测项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricMonitorPoint tBusElectricMonitorPoint = tBusElectricMonitorPointService.getById(id);
		return Result.ok(tBusElectricMonitorPoint);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricMonitorPoint
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricMonitorPoint tBusElectricMonitorPoint) {
      return super.exportXls(request, tBusElectricMonitorPoint, TBusElectricMonitorPoint.class, "监测项目");
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
      return super.importExcel(request, response, TBusElectricMonitorPoint.class);
  }

}
