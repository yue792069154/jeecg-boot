package org.jeecg.modules.system.sysRole.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 角色
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_role对象", description="角色")
public class SysRole {
    
	/**主键id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键id")
	private java.lang.String id;
	/**角色名称*/
	@Excel(name = "角色名称", width = 15)
    @ApiModelProperty(value = "角色名称")
	private java.lang.String roleName;
	/**角色入口协议类型编码*/
	@Excel(name = "角色入口协议类型编码", width = 15)
	@ApiModelProperty(value = "角色入口协议类型编码")
	private java.lang.String roleEntryProtCode;
	/**角色入口协议内容*/
	@Excel(name = "角色入口协议内容", width = 15)
	@ApiModelProperty(value = "角色入口协议内容")
	private java.lang.String roleEntryProtContent;
	/**角色编码*/
	@Excel(name = "角色编码", width = 15)
    @ApiModelProperty(value = "角色编码")
	private java.lang.String roleCode;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    @ApiModelProperty(value = "顺序")
	private java.lang.Integer sort;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String rem;
	/**启用状态*/
	@Excel(name = "启用状态", width = 15)
	@ApiModelProperty(value = "启用状态")
	private java.lang.String statusCode;
	/**删除状态*/
	@Excel(name = "删除状态", width = 15)
	@ApiModelProperty(value = "删除状态")
	@TableLogic
	private java.lang.String deleteCode;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updaterId;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createrId;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;

	@TableField(exist = false)
	private String keyword;
}
