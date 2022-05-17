

package com.px.modulars.meal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.basic.alone.core.base.BaseModel;

import javax.validation.constraints.*;

/**
 * 订餐记录
 *
 * @author px code generator
 * @date 2021-12-03 14:53:38
 */
@Data
@TableName("meal_record")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "订餐记录")
public class MealRecord extends BaseModel<MealRecord> {
    private static final long serialVersionUID = 1L;

    /**
     * 配餐信息id
     */
    @Size(max = 11, message = "配餐信息id超出长度11的限制")
    @ApiModelProperty(value = "配餐信息id")
    private Integer mealInfo;
    /**
     * 楼宇id
     */
    @Size(max = 11, message = "楼宇id超出长度11的限制")
    @ApiModelProperty(value = "楼宇id")
    private Integer buildingInfo;
    /**
     * 收餐人名字
     */
    @Size(max = 255, message = "收餐人名字超出长度255的限制")
    @ApiModelProperty(value = "收餐人名字")
    private String name;
    /**
     * 联系方式
     */
    @Size(max = 255, message = "联系方式超出长度255的限制")
    @ApiModelProperty(value = "联系方式")
    private String phone;
    /**
     * 详细地址
     */
    @Size(max = 255, message = "详细地址超出长度255的限制")
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "经度")
    private String lng;
    @ApiModelProperty(value = "纬度")
    private String lat;
    /**
     * 订餐类型（1.代叫2.自己叫）
     */
    @Size(max = 4, message = "订餐类型（1.代叫2.自己叫）超出长度4的限制")
    @ApiModelProperty(value = "订餐类型（1.代叫2.自己叫）")
    private Integer type;
    /**
     * 老人id
     */
    @Size(max = 11, message = "老人id超出长度11的限制")
    @ApiModelProperty(value = "老人id")
    private Integer oldMan;
    /**
     * 家人id
     */
    @Size(max = 20, message = "家人id超出长度20的限制")
    @ApiModelProperty(value = "家人id")
    private Long userId;
    /**
     * 完成状态（1.完成2.未完成）
     */
    @Size(max = 4, message = "完成状态（1.完成2.未完成）超出长度4的限制")
    @ApiModelProperty(value = "完成状态（1.完成2.未完成）")
    private Integer state;
    @ApiModelProperty(value = "配餐编号")
    private String mealNumber;
    @ApiModelProperty(value = "是否为设备订餐（1.是2.否）")
    private Integer isEquipment;
    @TableField(exist = false)
    private MealInfo mealInfoVo;
    @TableField(exist = false)
    private String buildingInfoName;
    @TableField(exist = false)
    private String stateName;
}
