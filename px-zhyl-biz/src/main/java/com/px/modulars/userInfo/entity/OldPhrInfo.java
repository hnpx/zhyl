

package com.px.modulars.userInfo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 老人健康档案
 *
 * @author XX
 * @date 2021-12-27 10:59:03
 */
@Data
@TableName("old_phr_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "老人健康档案")
public class OldPhrInfo extends BaseModel<OldPhrInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 测量时间
     */
    @Size(max = 255, message = "测量时间超出长度255的限制")
    @ApiModelProperty(value = "测量时间")
    @Excel(name = "测量时间")
    private String time;
    /**
     * 客户代码
     */
    @Size(max = 255, message = "客户代码超出长度255的限制")
    @ApiModelProperty(value = "客户代码")
    @Excel(name = "客户代码")
    private String usercode;
    /**
     * 客户姓名
     */
    @Size(max = 255, message = "客户姓名超出长度255的限制")
    @ApiModelProperty(value = "客户姓名")
    @Excel(name = "客户姓名")
    private String name;
    /**
     * 客户性别
     */
    @Size(max = 255, message = "客户性别超出长度255的限制")
    @ApiModelProperty(value = "客户性别")
    @Excel(name = "客户性别")
    private String sex;
    /**
     * 客户年龄
     */
    @Size(max = 255, message = "客户年龄超出长度255的限制")
    @ApiModelProperty(value = "客户年龄")
    @Excel(name = "客户年龄")
    private String age;
    /**
     * 备注信息
     */
    @Size(max = 255, message = "备注信息超出长度255的限制")
    @ApiModelProperty(value = "备注信息")
    @Excel(name = "备注信息")
    private String remarks;
    /**
     * 身高
     */
    @Size(max = 255, message = "身高超出长度255的限制")
    @ApiModelProperty(value = "身高")
    @Excel(name = "身高")
    private String height;
    /**
     * 体重
     */
    @Size(max = 255, message = "体重超出长度255的限制")
    @ApiModelProperty(value = "体重")
    @Excel(name = "体重")
    private String weight;
    /**
     * 体重指数
     */
    @Size(max = 255, message = "体重指数超出长度255的限制")
    @ApiModelProperty(value = "体重指数")
    @Excel(name = "体重指数")
    private String bmi;
    /**
     * 高压
     */
    @Size(max = 255, message = "高压超出长度255的限制")
    @ApiModelProperty(value = "高压")
    @Excel(name = "高压")
    private String bph;
    /**
     * 低压
     */
    @Size(max = 255, message = "低压超出长度255的限制")
    @ApiModelProperty(value = "低压")
    @Excel(name = "低压")
    private String bpl;
    /**
     * 心率
     */
    @Size(max = 255, message = "心率超出长度255的限制")
    @ApiModelProperty(value = "心率")
    @Excel(name = "心率")
    private String bpp;
    /**
     * 脂肪率
     */
    @Size(max = 255, message = "脂肪率超出长度255的限制")
    @ApiModelProperty(value = "脂肪率")
    @Excel(name = "脂肪率")
    private String fattbfr;
    /**
     * 脂肪量
     */
    @Size(max = 255, message = "脂肪量超出长度255的限制")
    @ApiModelProperty(value = "脂肪量")
    @Excel(name = "脂肪量")
    private String fatweight;
    /**
     * 基础代谢
     */
    @Size(max = 255, message = "基础代谢超出长度255的限制")
    @ApiModelProperty(value = "基础代谢")
    @Excel(name = "基础代谢")
    private String fatbmr;
    /**
     * 体水分率
     */
    @Size(max = 255, message = "体水分率超出长度255的限制")
    @ApiModelProperty(value = "体水分率")
    @Excel(name = "体水分率")
    private String attbwr;
    /**
     * 总水分
     */
    @Size(max = 255, message = "总水分超出长度255的限制")
    @ApiModelProperty(value = "总水分")
    @Excel(name = "总水分")
    private String fatswt;
    /**
     * 去脂体重
     */
    @Size(max = 255, message = "去脂体重超出长度255的限制")
    @ApiModelProperty(value = "去脂体重")
    @Excel(name = "去脂体重")
    private String fatsffm;
    /**
     * 骨骼肌率
     */
    @Size(max = 255, message = "骨骼肌率超出长度255的限制")
    @ApiModelProperty(value = "骨骼肌率")
    @Excel(name = "骨骼肌率")
    private String fatsmr;
    /**
     * 蛋白质
     */
    @Size(max = 255, message = "蛋白质超出长度255的限制")
    @ApiModelProperty(value = "蛋白质")
    @Excel(name = "蛋白质")
    private String fatsdbz;
    /**
     * 无机盐
     */
    @Size(max = 255, message = "无机盐超出长度255的限制")
    @ApiModelProperty(value = "无机盐")
    @Excel(name = "无机盐")
    private String fatswjy;
    /**
     * 内脏脂肪
     */
    @Size(max = 255, message = "内脏脂肪超出长度255的限制")
    @ApiModelProperty(value = "内脏脂肪")
    @Excel(name = "内脏脂肪")
    private String fatsvfi;
    /**
     * 骨矿含量
     */
    @Size(max = 255, message = "骨矿含量超出长度255的限制")
    @ApiModelProperty(value = "骨矿含量")
    @Excel(name = "骨矿含量")
    private String fatsbmc;
    /**
     * 体温
     */
    @Size(max = 255, message = "体温超出长度255的限制")
    @ApiModelProperty(value = "体温")
    @Excel(name = "体温")
    private String bt;
    /**
     * 血氧饱和度
     */
    @Size(max = 255, message = "血氧饱和度超出长度255的限制")
    @ApiModelProperty(value = "血氧饱和度")
    @Excel(name = "血氧饱和度")
    private String bo;
    /**
     * 老人id
     */
    @ApiModelProperty(value = "老人id")
    private Integer oldId;
}
