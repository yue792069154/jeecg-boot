package org.jeecg.modules.system.sysDict.entity;

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
 * @Description: 字典类型
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Data
@TableName("sys_dict_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_dict_type对象", description="字典类型")
public class SysDictType {
    
	/**主键字段*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键字段")
	private java.lang.String id;
	/**字典类型名称*/
	@Excel(name = "字典类型名称", width = 15)
    @ApiModelProperty(value = "字典类型名称")
	private java.lang.String dictTypeName;
	/**字典类型编码*/
	@Excel(name = "字典类型编码", width = 15)
    @ApiModelProperty(value = "字典类型编码")
	private java.lang.String dictTypeCode;
	/**扩展表名称*/
	@Excel(name = "扩展表名称", width = 15)
    @ApiModelProperty(value = "扩展表名称")
	private java.lang.String extendTableName;
	/**扩展表中文名*/
	@Excel(name = "扩展表中文名", width = 15)
    @ApiModelProperty(value = "扩展表中文名")
	private java.lang.String extendTableChineseName;
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

	public SysDictType() {
	}
}
