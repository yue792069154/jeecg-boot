package org.jeecg.modules.electrical.tBusElectricDeviceDatReal.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xpath.internal.objects.XObject;
import io.lettuce.core.AbstractRedisAsyncCommands;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatReal;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.entity.TBusElectricDeviceDatRealVO;
import org.jeecg.modules.electrical.tBusElectricDeviceDatReal.service.ITBusElectricDeviceDatRealService;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 实时数据
 * @Author: LE WEN
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="实时数据")
@RestController
@RequestMapping("/bus/tBusElectricDeviceDatReal")
public class TBusElectricDeviceDatRealController extends JeecgController<TBusElectricDeviceDatReal, ITBusElectricDeviceDatRealService> {
	@Autowired
	private ITBusElectricDeviceDatRealService tBusElectricDeviceDatRealService;

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
	@AutoLog(value = "实时数据-分页列表查询")
	@ApiOperation(value="实时数据-分页列表查询", notes="实时数据-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");
		String deviceCode=httpServletRequest.getParameter("deviceCode");
		String cache=httpServletRequest.getParameter("cache");

		Page<TBusElectricDeviceDatReal> pageList = new Page<TBusElectricDeviceDatReal>(pageNo, pageSize);

		if(cache.equals("false")){
			pageList = tBusElectricDeviceDatRealService.getDeviceDatRealPageList(pageList,keyword,deviceCode);
			redisUtil.lSet("tBusElectricDeviceDatReal_list_"+deviceCode+pageNo+keyword, pageList,600);
			return Result.ok(pageList);
		}else {
			List<Object> redisPageList=redisUtil.lGet("tBusElectricDeviceDatReal_list_"+deviceCode+pageNo+keyword, 0,-1);
			if (redisPageList.size()>0){
				return Result.ok(redisPageList.get(0));
			}else {
				pageList = tBusElectricDeviceDatRealService.getDeviceDatRealPageList(pageList,keyword,deviceCode);
				redisUtil.lSet("tBusElectricDeviceDatReal_list_"+deviceCode+pageNo+keyword, pageList,600);
				return Result.ok(pageList);
			}
		}

	}



	 /**
	  * 分页列表查询
	  *
	  * @param httpServletRequest
	  * @param pageNo
	  * @param pageSize
	  * @return
	  */
	 @AutoLog(value = "历史数据-分页列表查询")
	 @ApiOperation(value="历史数据-分页列表查询", notes="历史数据-分页列表查询")
	 @GetMapping(value = "/historyList")
	 public Result<?> queryHistoryPageList(HttpServletRequest httpServletRequest,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		 String keyword=httpServletRequest.getParameter("keyword");
		 String deviceCode=httpServletRequest.getParameter("deviceCode");

		 String monitorStartTime=httpServletRequest.getParameter("monitorStartTime");
		 String monitorEndTime=httpServletRequest.getParameter("monitorEndTime");
		 String cache=httpServletRequest.getParameter("cache");

		 Page<TBusElectricDeviceDatReal> pageList = new Page<TBusElectricDeviceDatReal>(pageNo, pageSize);

		 String redisKey="tBusElectricDeviceDatReal_historyList_"+deviceCode+pageNo+keyword+monitorStartTime+monitorEndTime;

		 if(cache.equals("false")){
			 pageList = tBusElectricDeviceDatRealService.getDeviceHistoryPageList(pageList,keyword,deviceCode,monitorStartTime,monitorEndTime);
			 redisUtil.lSet(redisKey, pageList,600);
			 return Result.ok(pageList);
		 }else {
			 List<Object> redisPageList=redisUtil.lGet(redisKey, 0,-1);
			 if (redisPageList.size()>0){
				 return Result.ok(redisPageList.get(0));
			 }else {
				 pageList = tBusElectricDeviceDatRealService.getDeviceHistoryPageList(pageList,keyword,deviceCode,monitorStartTime,monitorEndTime);
				 redisUtil.lSet(redisKey, pageList,600);
				 return Result.ok(pageList);
			 }
		 }



	 }
	
	/**
	 * 添加
	 *
	 * @param tBusElectricDeviceDatReal
	 * @return
	 */
	@AutoLog(value = "实时数据-添加")
	@ApiOperation(value="实时数据-添加", notes="实时数据-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricDeviceDatReal tBusElectricDeviceDatReal) {
		tBusElectricDeviceDatRealService.save(tBusElectricDeviceDatReal);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricDeviceDatReal
	 * @return
	 */
	@AutoLog(value = "实时数据-编辑")
	@ApiOperation(value="实时数据-编辑", notes="实时数据-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricDeviceDatReal tBusElectricDeviceDatReal) {
		tBusElectricDeviceDatRealService.updateById(tBusElectricDeviceDatReal);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "实时数据-通过id删除")
	@ApiOperation(value="实时数据-通过id删除", notes="实时数据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tBusElectricDeviceDatRealService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "实时数据-批量删除")
	@ApiOperation(value="实时数据-批量删除", notes="实时数据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricDeviceDatRealService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "实时数据-通过id查询")
	@ApiOperation(value="实时数据-通过id查询", notes="实时数据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricDeviceDatReal tBusElectricDeviceDatReal = tBusElectricDeviceDatRealService.getById(id);
		return Result.ok(tBusElectricDeviceDatReal);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricDeviceDatReal
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricDeviceDatReal tBusElectricDeviceDatReal) {
      return super.exportXls(request, tBusElectricDeviceDatReal, TBusElectricDeviceDatReal.class, "实时数据");
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
      return super.importExcel(request, response, TBusElectricDeviceDatReal.class);
  }

}
