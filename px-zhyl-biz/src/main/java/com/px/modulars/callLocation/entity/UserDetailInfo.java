

package com.px.modulars.callLocation.entity;

import com.px.basic.alone.core.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实时定位-老人详情
 *
 * @author px code generator
 * @date 2021-12-06 10:41:33
 */
@Data
public class UserDetailInfo  {

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceNumber;
    /**
     * 设备卡号
     */
    @ApiModelProperty(value = "设备卡号")
    private String deviceCard;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;

    /**
     * 老人姓名
     */
    @ApiModelProperty(value = "老人姓名")
    private String name;

    /**
     * 老人手机号
     */
    @ApiModelProperty(value = "老人手机号")
    private String phone;

    /**
     * 老人健康状态
     */
    @ApiModelProperty(value = "老人健康状态")
    private String physical;



    /**
     * 老人年龄
     */
    @ApiModelProperty(value = "老人年龄")
    private String age;


    /**
     * 老人ID
     */
    @ApiModelProperty(value = "老人ID")
    private String oldId;



}
