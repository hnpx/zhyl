

package com.px.modulars.device.entity;

import cn.hutool.core.date.DateTime;
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
 * 设备信息
 *
 * @author px code generator
 * @date 2021-12-02 13:34:52
 */
@Data
@TableName("device_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备信息")
public class DeviceInfo extends BaseModel<DeviceInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @Size(max = 255, message = "设备编号超出长度255的限制")
    @ApiModelProperty(value = "设备编号")
    private String deviceNumber;
    /**
     * 设备卡号
     */
    @Size(max = 255, message = "设备卡号超出长度255的限制")
    @ApiModelProperty(value = "设备卡号")
    private String deviceCard;
    /**
     * 设备状态
     * 0，正常运行
     * 1，网络异常
     * 2，电量异常
     */
    @Size(max = 255, message = "设备状态 0，正常运行 1，网络异常 2，电量异常超出长度255的限制")
    @ApiModelProperty(value = "设备状态 0，正常运行 1，网络异常 2，电量异常")
    private String deviceStatus;
    /**
     * 绑定状态
     * 0，绑定
     * 1，未绑定
     */
    @Size(max = 255, message = "绑定状态 0，绑定 1，未绑定超出长度255的限制")
    @ApiModelProperty(value = "绑定状态 0，绑定 1，未绑定")
    private String bindingStatus;
    /**
     * 绑定人名字
     */
    @Size(max = 255, message = "绑定人名字超出长度255的限制")
    @ApiModelProperty(value = "绑定人名字")
    private String bindingName;
    /**
     * 绑定人id
     */
   /* @Size(max = 11, message = "绑定人id超出长度11的限制")*/
    @ApiModelProperty(value = "绑定人id")
    private Integer bindingId;
    /**
     * 最后一次链接时间
     */
    @ApiModelProperty(value = "最后一次链接时间")
    private LocalDateTime lastUpdateTime;
}
