package org.jeecg.modules.electrical.tBusElectricDevice.controller;

import java.util.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.electrical.tBusElectricDevice.entity.TBusElectricDeviceMonitorPoint;
import org.jeecg.modules.electrical.tBusElectricDevice.service.ITBusElectricDeviceMonitorPointService;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 设备监测项目
 * @Author: LE WEN
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备监测项目")
@RestController
@RequestMapping("/bus/tBusElectricDeviceMonitorPoint")
public class TBusElectricDeviceMonitorPointController extends JeecgController<TBusElectricDeviceMonitorPoint, ITBusElectricDeviceMonitorPointService> {
	@Autowired
	private ITBusElectricDeviceMonitorPointService tBusElectricDeviceMonitorPointService;

	 @Autowired
	 @Lazy
	 private RedisUtil redisUtil;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "设备监测项目-分页列表查询")
	@ApiOperation(value="设备监测项目-分页列表查询", notes="设备监测项目-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");

		QueryWrapper<TBusElectricDeviceMonitorPoint> queryWrapper = new QueryWrapper<>();

		Page<TBusElectricDeviceMonitorPoint> page = new Page<TBusElectricDeviceMonitorPoint>(pageNo, pageSize);
		IPage<TBusElectricDeviceMonitorPoint> pageList = tBusElectricDeviceMonitorPointService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 /**
	  * 列表查询
	  *
	  * @param tBusElectricDeviceMonitorPoint
	  * @return
	  */
	 @AutoLog(value = "功能-列表查询")
	 @ApiOperation(value="功能-列表查询", notes="功能-列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint) {
		 return Result.ok(tBusElectricDeviceMonitorPointService.queryList(tBusElectricDeviceMonitorPoint));
	 }


	 /**
	 * 添加
	 *
	 * @param tBusElectricDeviceMonitorPoint
	 * @return
	 */
	@AutoLog(value = "设备监测项目-添加")
	@ApiOperation(value="设备监测项目-添加", notes="设备监测项目-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint) {
		tBusElectricDeviceMonitorPointService.save(tBusElectricDeviceMonitorPoint);
		redisUtil.delByKeyword("tBusElectricDeviceDatReal");
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricDeviceMonitorPoint
	 * @return
	 */
	@AutoLog(value = "设备监测项目-编辑")
	@ApiOperation(value="设备监测项目-编辑", notes="设备监测项目-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint) {
		tBusElectricDeviceMonitorPointService.updateById(tBusElectricDeviceMonitorPoint);
		return Result.ok("编辑成功!");
	}
	

	 /**
	  * 通过id删除
	  *
	  * @param deviceCode
	  * @param pointCode
	  * @return
	  */
	 @AutoLog(value = "监测项目-通过deviceCode、pointCode删除")
	 @ApiOperation(value="监测项目-通过deviceCode、pointCode删除", notes="监测项目-通过deviceCode、pointCode删除")
	 @DeleteMapping(value = "/delete")
	 public Result<?> delete(@RequestParam(name="deviceCode",required=true) String deviceCode,@RequestParam(name="pointCode",required=true) String pointCode) {
		 QueryWrapper<TBusElectricDeviceMonitorPoint> queryWrapper =new QueryWrapper<>();
		 queryWrapper.eq("device_code",deviceCode).eq("point_id",pointCode);
		 tBusElectricDeviceMonitorPointService.remove(queryWrapper);
		 redisUtil.delByKeyword("tBusElectricDeviceDatReal");
		 return Result.ok("删除成功!");
	 }
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备监测项目-批量删除")
	@ApiOperation(value="设备监测项目-批量删除", notes="设备监测项目-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricDeviceMonitorPointService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备监测项目-通过id查询")
	@ApiOperation(value="设备监测项目-通过id查询", notes="设备监测项目-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint = tBusElectricDeviceMonitorPointService.getById(id);
		return Result.ok(tBusElectricDeviceMonitorPoint);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricDeviceMonitorPoint
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricDeviceMonitorPoint tBusElectricDeviceMonitorPoint) {
      return super.exportXls(request, tBusElectricDeviceMonitorPoint, TBusElectricDeviceMonitorPoint.class, "设备监测项目");
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
      return super.importExcel(request, response, TBusElectricDeviceMonitorPoint.class);
  }

}
