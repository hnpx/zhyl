

package com.px.modulars.upms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.basic.alone.core.base.BaseModel;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统APP版本
 *
 * @author px code generator
 * @date 2021-06-25 15:34:14
 */
@Data
@TableName("sys_app")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统APP版本")
public class SysApp extends BaseModel<SysApp> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * app名称
     */
    @Size(max = 255, message = "APP名称超出长度255的限制")
    @ApiModelProperty(value = "app名称")
    private String name;
    /**
     * appkey
     */
    @Size(max = 255, message = "APPKEY超出长度255的限制")
    @ApiModelProperty(value = "appkey")
    private String appkey;
    /**
     * 类型：1-web,2-小程序，3-安卓，4-ios
     */
    @ApiModelProperty(value = "类型：1-web,2-小程序，3-安卓，4-ios")
    private Integer type;
    /**
     * 当前版本号
     */
    @Size(max = 255, message = "当前版本号超出长度255的限制")
    @ApiModelProperty(value = "当前版本号")
    private String version;
    /**
     * 当前更新内容
     */
    @ApiModelProperty(value = "当前更新内容")
    private String content;
}
