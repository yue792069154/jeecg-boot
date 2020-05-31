package org.jeecg.modules.system.sysMenu.entity;

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
 * @Description: 菜单
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Data
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_menu对象", description="菜单")
public class SysMenu {
    
	/**菜单标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "菜单标识")
	private java.lang.String id;
	/**菜单名称*/
	@Excel(name = "菜单名称", width = 15)
    @ApiModelProperty(value = "菜单名称")
	private java.lang.String menuName;
	/**菜单编码*/
	@Excel(name = "菜单编码", width = 15)
    @ApiModelProperty(value = "菜单编码")
	private java.lang.String menuCode;
	/**菜单编码*/
	@Excel(name = "菜单编码", width = 15)
    @ApiModelProperty(value = "菜单编码")
	private java.lang.String parentMenuId;
	/**菜单入口协议类型编码*/
	@Excel(name = "菜单入口协议类型编码", width = 15)
    @ApiModelProperty(value = "菜单入口协议类型编码")
	private java.lang.String menuEntryProtCode;
	/**菜单入口协议内容（json格式）*/
	@Excel(name = "菜单入口协议内容（json格式）", width = 15)
    @ApiModelProperty(value = "菜单入口协议内容（json格式）")
	private java.lang.String menuEntryProtContent;
	/**菜单打开类型*/
	@Excel(name = "菜单打开类型", width = 15)
    @ApiModelProperty(value = "菜单打开类型")
	private java.lang.String menuOpenTypeCode;
	/**菜单图标协议内容*/
	@Excel(name = "菜单图标协议内容", width = 15)
    @ApiModelProperty(value = "菜单图标协议内容")
	private java.lang.String menuIconProtContent;
	/**菜单设置内容（json格式）*/
	@Excel(name = "菜单设置内容（json格式）", width = 15)
    @ApiModelProperty(value = "菜单设置内容（json格式）")
	private java.lang.String menuSettings;
	/**菜单样式*/
	@Excel(name = "菜单样式", width = 15)
    @ApiModelProperty(value = "菜单样式")
	private java.lang.String menuClass;
	/**菜单类型*/
	@Excel(name = "菜单类型", width = 15)
    @ApiModelProperty(value = "菜单类型")
	private java.lang.String menuStyle;
	/**菜单描述*/
	@Excel(name = "菜单描述", width = 15)
    @ApiModelProperty(value = "菜单描述")
	private java.lang.String menuDesc;
	/**顺序*/
	@Excel(name = "顺序", width = 15)
    @ApiModelProperty(value = "顺序")
	private java.lang.Integer sort;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String rem;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
	@TableLogic
	private java.lang.String statusCode;
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
}
