

package com.px.modulars.donations.entity;

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
 * 捐资捐物
 *
 * @author px code generator
 * @date 2021-12-13 10:06:00
 */
@Data
@TableName("donations_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "捐资捐物")
public class DonationsInfo extends BaseModel<DonationsInfo> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Size(max = 11, message = "超出长度11的限制")
    @ApiModelProperty(value = "")
    private Long allUserId;

    /**
     * 姓名
     */
    @Size(max = 255, message = "姓名超出长度255的限制")
    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 联系方式
     */
    @Size(max = 255, message = "联系方式超出长度255的限制")
    @ApiModelProperty(value = "联系方式")
    private String phone;
    /**
     * 所在单位
     */
    @Size(max = 255, message = "所在单位超出长度255的限制")
    @ApiModelProperty(value = "所在单位")
    private String company;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String explainText;
    /**
     * 附件（图片）
     */
    @ApiModelProperty(value = "附件（图片）")
    private String attachment;
    /**
     * 视频
     */
    @ApiModelProperty(value = "视频")
    private String video;
    /**
     * 捐赠方式
     * 1，捐资物资
     * 2，捐资现金
     */
    @ApiModelProperty(value = "捐赠方式 1，捐资物资 2，捐资现金")
    private Integer donationMethod;
    /**
     * 参与方式
     * 1，个体
     * 2，团体
     */
    @ApiModelProperty(value = "参与方式 1，个体 2，团体")
    private Integer participationMethod;
}
