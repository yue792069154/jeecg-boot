package org.jeecg.modules.system.sysUser.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.sysUser.entity.SysUser;
import org.jeecg.modules.system.sysUser.service.ISysUserService;
import java.util.Date;
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

@Slf4j
@Api(tags="用户")
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController extends JeecgController<SysUser, ISysUserService> {
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysUser
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "用户-分页列表查询")
	@ApiOperation(value="用户-分页列表查询", notes="用户-分页列表查询")
	@PostMapping(value = "/list")
	public Result<?> queryPageList(@RequestBody SysUser sysUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		QueryWrapper<SysUser> queryWrapper =new QueryWrapper<>();
		queryWrapper.and(wrapper -> wrapper.like("user_name",sysUser.getUserName()))
		.or().eq(!StringUtil.isNullOrEmpty(sysUser.getStatusCode()),"status_code",sysUser.getStatusCode());
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 列表查询
	 *
	 * @param sysUser
	 * @return
	 */
	@AutoLog(value = "用户-列表查询")
	@ApiOperation(value="用户-列表查询", notes="用户-列表查询")
	@PostMapping(value = "/queryall")
	public Result<?> queryList(@RequestBody SysUser sysUser) {
		return Result.ok(sysUserService.queryList());
	}

	
	/**
	 * 添加
	 *
	 * @param sysUser
	 * @return
	 */
	@AutoLog(value = "用户-添加")
	@ApiOperation(value="用户-添加", notes="用户-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SysUser sysUser) {

		Result<SysUser> result = new Result<SysUser>();

		try {
			sysUser.setCreateTime(new Date());//设置创建时间
			String salt = oConvertUtils.randomGen(8);
			sysUser.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(sysUser.getUserName(), sysUser.getPassword(), salt);
			sysUser.setPassword(passwordEncode);
			sysUser.setStatusCode("0");
			Boolean saveStatus=sysUserService.save(sysUser);
			if (saveStatus){
				sysUserService.addUserRoleAndOrg(sysUser);
			}
			result.success("添加成功！");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}

		return result;
	}
	
	/**
	 * 编辑
	 *
	 * @param sysUser
	 * @return
	 */
	@AutoLog(value = "用户-编辑")
	@ApiOperation(value="用户-编辑", notes="用户-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SysUser sysUser) {
		Result<SysUser> result = new Result<SysUser>();
		try {
			SysUser user = sysUserService.getById(sysUser.getId());
			if(user==null) {
				result.error500("未找到对应实体");
			}else {
				sysUser.setUpdateTime(new Date());
				sysUser.setPassword(user.getPassword());
				Boolean saveStatus=sysUserService.updateById(sysUser);
				if (saveStatus){
					sysUserService.editUserRoleAndOrg(sysUser);
				}
				result.success("修改成功!");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "用户-通过id删除")
	@ApiOperation(value="用户-通过id删除", notes="用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sysUserService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "用户-批量删除")
	@ApiOperation(value="用户-批量删除", notes="用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	 /**
	  * 冻结&解冻用户
	  * @param sysUserList
	  * @return
	  */
	 @RequestMapping(value = "/frozenBatch", method = RequestMethod.PUT)
	 public Result<SysUser> frozenBatch(@RequestBody List<SysUser> sysUserList,@RequestParam(name="statusCode",required=true) String statusCode) {
		 Result<SysUser> result = new Result<SysUser>();
		 try {

			 for (SysUser sysUser : sysUserList) {
				 this.sysUserService.update(new SysUser().setStatusCode(statusCode),
						 new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,sysUser.getId()));
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
	@AutoLog(value = "用户-通过id查询")
	@ApiOperation(value="用户-通过id查询", notes="用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SysUser sysUser = sysUserService.getById(id);
		return Result.ok(sysUser);
	}

	 /**
	  * 修改密码
	  */
	 @RequestMapping(value = "/changPassword", method = RequestMethod.PUT)
	 public Result<?> changPassword(@RequestBody SysUser sysUser) {
		 SysUser user = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, sysUser.getUserName()));
		 if (user == null) {
			 return Result.error("用户不存在！");
		 }
		 sysUser.setId(user.getId());
		 Boolean isSuccess=sysUserService.changePassword(sysUser);
		 if(isSuccess){
			return Result.ok("密码修改成功!");
		 }else
		 {
		 	return Result.error("密码修改失败!");
		 }

	 }


  /**
   * 导出excel
   *
   * @param request
   * @param sysUser
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, SysUser sysUser) {
      return super.exportXls(request, sysUser, SysUser.class, "用户");
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
      return super.importExcel(request, response, SysUser.class);
  }

}
