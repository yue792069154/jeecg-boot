package org.jeecg.modules.electrical.tBusElectricDevice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.electrical.tBusElectricDevice.entity.TBusElectricDevice;
import org.jeecg.modules.electrical.tBusElectricDevice.service.ITBusElectricDeviceService;
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
 * @Description: 设备中心
 * @Author: LE WEN
 * @Date:   2020-07-05
 * @Version: V1.0
 */
@Slf4j
@Api(tags="设备中心")
@RestController
@RequestMapping("/bus/tBusElectricDevice")
public class TBusElectricDeviceController extends JeecgController<TBusElectricDevice, ITBusElectricDeviceService> {
	@Autowired
	private ITBusElectricDeviceService tBusElectricDeviceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "设备中心-分页列表查询")
	@ApiOperation(value="设备中心-分页列表查询", notes="设备中心-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");
		String statusCode=httpServletRequest.getParameter("statusCode");
		String orgId=httpServletRequest.getParameter("orgId");

		QueryWrapper<TBusElectricDevice> queryWrapper = new QueryWrapper<>();
		queryWrapper.like(!StringUtil.isNullOrEmpty(keyword),"device_name",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_code",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_manufacturer",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_model",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_power",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_rate",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_line",keyword)
				.or()
				.like(!StringUtil.isNullOrEmpty(keyword),"device_install",keyword)
				.eq(!StringUtil.isNullOrEmpty(orgId),"org_id",orgId)
				.eq(!StringUtil.isNullOrEmpty(statusCode),"status_code",statusCode);
		Page<TBusElectricDevice> page = new Page<TBusElectricDevice>(pageNo, pageSize);
		IPage<TBusElectricDevice> pageList = tBusElectricDeviceService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


     /**
      * 列表查询
      *
      * @param httpServletRequest
      * @return
      */
     @AutoLog(value = "设备中心-列表查询")
     @ApiOperation(value="设备中心-列表查询", notes="设备中心-列表查询")
     @GetMapping(value = "/queryall")
     public Result<?> queryList(HttpServletRequest httpServletRequest) {
         return Result.ok(tBusElectricDeviceService.list());
     }
	
	/**
	 * 添加
	 *
	 * @param tBusElectricDevice
	 * @return
	 */
	@AutoLog(value = "设备中心-添加")
	@ApiOperation(value="设备中心-添加", notes="设备中心-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricDevice tBusElectricDevice) {
		tBusElectricDeviceService.save(tBusElectricDevice);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricDevice
	 * @return
	 */
	@AutoLog(value = "设备中心-编辑")
	@ApiOperation(value="设备中心-编辑", notes="设备中心-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricDevice tBusElectricDevice) {
		tBusElectricDeviceService.updateById(tBusElectricDevice);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备中心-通过id删除")
	@ApiOperation(value="设备中心-通过id删除", notes="设备中心-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tBusElectricDeviceService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备中心-批量删除")
	@ApiOperation(value="设备中心-批量删除", notes="设备中心-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricDeviceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	 /**
	  * 停用&启用设备
	  * @param ids
	  * @return
	  */
	 @GetMapping(value = "/frozenBatch")
	 public Result<TBusElectricDevice> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="statusCode",required=true) String statusCode) {
		 Result<TBusElectricDevice> result = new Result<TBusElectricDevice>();
		 String [] idList = ids.split(",");
		 try {
			 for (String id : idList) {
				 this.tBusElectricDeviceService.update(new TBusElectricDevice().setStatusCode(statusCode),
						 new UpdateWrapper<TBusElectricDevice>().lambda().eq(TBusElectricDevice::getId,id));
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
	@AutoLog(value = "设备中心-通过id查询")
	@ApiOperation(value="设备中心-通过id查询", notes="设备中心-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricDevice tBusElectricDevice = tBusElectricDeviceService.getById(id);
		return Result.ok(tBusElectricDevice);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricDevice
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricDevice tBusElectricDevice) {
      return super.exportXls(request, tBusElectricDevice, TBusElectricDevice.class, "设备中心");
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
      return super.importExcel(request, response, TBusElectricDevice.class);
  }

}
