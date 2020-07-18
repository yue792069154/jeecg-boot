package org.jeecg.modules.electrical.tBusElectricDeviceWarning.controller;

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
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.entity.TBusElectricDeviceWarningHandle;
import org.jeecg.modules.electrical.tBusElectricDeviceWarning.service.ITBusElectricDeviceWarningHandleService;
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
 * @Description: 告警中心
 * @Author: LE WEN
 * @Date:   2020-07-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="告警中心")
@RestController
@RequestMapping("/bus/tBusElectricDeviceWarningHandle")
public class TBusElectricDeviceWarningHandleController extends JeecgController<TBusElectricDeviceWarningHandle, ITBusElectricDeviceWarningHandleService> {
	@Autowired
	private ITBusElectricDeviceWarningHandleService tBusElectricDeviceWarningHandleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "告警中心-分页列表查询")
	@ApiOperation(value="告警中心-分页列表查询", notes="告警中心-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");
		String warningId=httpServletRequest.getParameter("warningId");

		QueryWrapper<TBusElectricDeviceWarningHandle> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(!StringUtil.isNullOrEmpty(warningId),"warning_id",warningId);

		Page<TBusElectricDeviceWarningHandle> page = new Page<TBusElectricDeviceWarningHandle>(pageNo, pageSize);
		IPage<TBusElectricDeviceWarningHandle> pageList = tBusElectricDeviceWarningHandleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 /**
	  * 列表查询
	  *
	  * @param httpServletRequest
	  * @return
	  */
	 @AutoLog(value = "告警中心-列表查询")
	 @ApiOperation(value="告警中心-列表查询", notes="告警中心-列表查询")
	 @GetMapping(value = "/queryall")
	 public Result<?> queryList(HttpServletRequest httpServletRequest) {
		 String warningId=httpServletRequest.getParameter("warningId");
		 return Result.ok(tBusElectricDeviceWarningHandleService.getDeviceWarningHandleList(warningId));
	 }
	
	/**
	 * 添加
	 *
	 * @param tBusElectricDeviceWarningHandle
	 * @return
	 */
	@AutoLog(value = "告警中心-添加")
	@ApiOperation(value="告警中心-添加", notes="告警中心-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricDeviceWarningHandle tBusElectricDeviceWarningHandle) {
		tBusElectricDeviceWarningHandleService.save(tBusElectricDeviceWarningHandle);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricDeviceWarningHandle
	 * @return
	 */
	@AutoLog(value = "告警中心-编辑")
	@ApiOperation(value="告警中心-编辑", notes="告警中心-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricDeviceWarningHandle tBusElectricDeviceWarningHandle) {
		tBusElectricDeviceWarningHandleService.updateById(tBusElectricDeviceWarningHandle);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "告警中心-通过id删除")
	@ApiOperation(value="告警中心-通过id删除", notes="告警中心-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tBusElectricDeviceWarningHandleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "告警中心-批量删除")
	@ApiOperation(value="告警中心-批量删除", notes="告警中心-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricDeviceWarningHandleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "告警中心-通过id查询")
	@ApiOperation(value="告警中心-通过id查询", notes="告警中心-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricDeviceWarningHandle tBusElectricDeviceWarningHandle = tBusElectricDeviceWarningHandleService.getById(id);
		return Result.ok(tBusElectricDeviceWarningHandle);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricDeviceWarningHandle
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricDeviceWarningHandle tBusElectricDeviceWarningHandle) {
      return super.exportXls(request, tBusElectricDeviceWarningHandle, TBusElectricDeviceWarningHandle.class, "告警中心");
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
      return super.importExcel(request, response, TBusElectricDeviceWarningHandle.class);
  }

}
