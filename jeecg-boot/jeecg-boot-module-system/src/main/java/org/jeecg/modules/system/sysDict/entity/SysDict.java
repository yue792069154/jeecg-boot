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
 * @Description: 字典
 * @Author: jeecg-boot
 * @Date:   2020-05-30
 * @Version: V1.0
 */
@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_dict对象", description="字典")
public class SysDict {
    
	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
	private java.lang.String id;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
	private java.lang.String dictName;
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
	private java.lang.String dictCode;
	/**父级编码*/
	@Excel(name = "父级编码", width = 15)
    @ApiModelProperty(value = "父级编码")
	private java.lang.String parentCode;
	/**字典类型编码*/
	@Excel(name = "字典类型编码", width = 15)
    @ApiModelProperty(value = "字典类型编码")
	private java.lang.String dictTypeCode;
	/**拼音*/
	@Excel(name = "拼音", width = 15)
    @ApiModelProperty(value = "拼音")
	private java.lang.String pinyin;
	/**等级*/
	@Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
	private java.lang.Integer level;
	/**是否末级*/
	@Excel(name = "是否末级", width = 15)
    @ApiModelProperty(value = "是否末级")
	private java.lang.String isEndNode;
	/**全编码*/
	@Excel(name = "全编码", width = 15)
    @ApiModelProperty(value = "全编码")
	private java.lang.String fullCode;
	/**全名称*/
	@Excel(name = "全名称", width = 15)
    @ApiModelProperty(value = "全名称")
	private java.lang.String fullName;
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
