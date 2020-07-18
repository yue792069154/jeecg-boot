package org.jeecg.modules.electrical.tBusElectricOrg.controller;

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
import org.jeecg.modules.electrical.tBusElectricOrg.entity.TBusElectricOrg;
import org.jeecg.modules.electrical.tBusElectricOrg.service.ITBusElectricOrgService;
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
 * @Description: 机构管理
 * @Author: LE WEN
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="机构管理")
@RestController
@RequestMapping("/bus/tBusElectricOrg")
public class TBusElectricOrgController extends JeecgController<TBusElectricOrg, ITBusElectricOrgService> {
	@Autowired
	private ITBusElectricOrgService tBusElectricOrgService;
	
	/**
	 * 分页列表查询
	 *
	 * @param httpServletRequest
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "机构管理-分页列表查询")
	@ApiOperation(value="机构管理-分页列表查询", notes="机构管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest httpServletRequest,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		String keyword=httpServletRequest.getParameter("keyword");

		QueryWrapper<TBusElectricOrg> queryWrapper = new QueryWrapper<>();

		Page<TBusElectricOrg> page = new Page<TBusElectricOrg>(pageNo, pageSize);
		IPage<TBusElectricOrg> pageList = tBusElectricOrgService.page(page, queryWrapper);
		return Result.ok(pageList);
	}


	 /**
	  * 全部列表查询
	  *
	  * @param tBusElectricOrg
	  * @return
	  */
	 @AutoLog(value = "机构-全部列表查询")
	 @ApiOperation(value="机构-全部列表查询", notes="机构-全部列表查询")
	 @PostMapping(value = "/queryall")
	 public Result<?> queryList(@RequestBody TBusElectricOrg tBusElectricOrg) {
		 QueryWrapper<TBusElectricOrg> queryWrapper = new QueryWrapper<>();
		 queryWrapper
				 .like(!StringUtil.isNullOrEmpty(tBusElectricOrg.getKeyword()),"org_name",tBusElectricOrg.getKeyword())
				 .eq(!StringUtil.isNullOrEmpty(tBusElectricOrg.getStatusCode()),"status_code",tBusElectricOrg.getStatusCode())
				 .orderByAsc("sort");
		 List<TBusElectricOrg> tBusElectricOrgList = tBusElectricOrgService.list(queryWrapper);
		 return Result.ok(tBusElectricOrgList);
	 }
	
	/**
	 * 添加
	 *
	 * @param tBusElectricOrg
	 * @return
	 */
	@AutoLog(value = "机构管理-添加")
	@ApiOperation(value="机构管理-添加", notes="机构管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TBusElectricOrg tBusElectricOrg) {
		tBusElectricOrgService.save(tBusElectricOrg);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param tBusElectricOrg
	 * @return
	 */
	@AutoLog(value = "机构管理-编辑")
	@ApiOperation(value="机构管理-编辑", notes="机构管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TBusElectricOrg tBusElectricOrg) {
		tBusElectricOrgService.updateById(tBusElectricOrg);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "机构管理-通过id删除")
	@ApiOperation(value="机构管理-通过id删除", notes="机构管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		tBusElectricOrgService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "机构管理-批量删除")
	@ApiOperation(value="机构管理-批量删除", notes="机构管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.tBusElectricOrgService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}


	 /**
	  * 启用&停用机构
	  * @param ids
	  * @return
	  */
	 @GetMapping(value = "/frozenBatch")
	 public Result<TBusElectricOrg> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="statusCode",required=true) String statusCode) {
		 Result<TBusElectricOrg> result = new Result<TBusElectricOrg>();
		 String [] idList = ids.split(",");
		 try {
			 for (String id : idList) {
				 this.tBusElectricOrgService.update(new TBusElectricOrg().setStatusCode(statusCode),
						 new UpdateWrapper<TBusElectricOrg>().lambda().eq(TBusElectricOrg::getId,id));
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
	@AutoLog(value = "机构管理-通过id查询")
	@ApiOperation(value="机构管理-通过id查询", notes="机构管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TBusElectricOrg tBusElectricOrg = tBusElectricOrgService.getById(id);
		return Result.ok(tBusElectricOrg);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param tBusElectricOrg
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, TBusElectricOrg tBusElectricOrg) {
      return super.exportXls(request, tBusElectricOrg, TBusElectricOrg.class, "机构管理");
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
      return super.importExcel(request, response, TBusElectricOrg.class);
  }

}
