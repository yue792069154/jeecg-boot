package org.jeecg.modules.system.sysLog.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog implements Serializable {

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**日志类型（1登录日志，2操作日志）*/
	@Excel(name = "日志类型（1登录日志，2操作日志）", width = 15)
	@ApiModelProperty(value = "日志类型（1登录日志，2操作日志）")
	private java.lang.Integer logType;
	/**日志内容*/
	@Excel(name = "日志内容", width = 15)
	@ApiModelProperty(value = "日志内容")
	private java.lang.String logContent;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
	@ApiModelProperty(value = "操作类型")
	private java.lang.Integer operateType;
	/**操作用户账号*/
	@Excel(name = "操作用户账号", width = 15)
	@ApiModelProperty(value = "操作用户账号")
	private java.lang.String userId;
	/**操作用户名称*/
	@Excel(name = "操作用户名称", width = 15)
	@ApiModelProperty(value = "操作用户名称")
	private java.lang.String userName;
	/**IP*/
	@Excel(name = "IP", width = 15)
	@ApiModelProperty(value = "IP")
	private java.lang.String ip;
	/**请求java方法*/
	@Excel(name = "请求java方法", width = 15)
	@ApiModelProperty(value = "请求java方法")
	private java.lang.String method;
	/**请求路径*/
	@Excel(name = "请求路径", width = 15)
	@ApiModelProperty(value = "请求路径")
	private java.lang.String requestUrl;
	/**请求参数*/
	@Excel(name = "请求参数", width = 15)
	@ApiModelProperty(value = "请求参数")
	private java.lang.Object requestParam;
	/**请求类型*/
	@Excel(name = "请求类型", width = 15)
	@ApiModelProperty(value = "请求类型")
	private java.lang.String requestType;
	/**耗时*/
	@Excel(name = "耗时", width = 15)
	@ApiModelProperty(value = "耗时")
	private java.lang.Integer costTime;
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
