

package com.px.modulars.generalization.entity;

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
 * 中心概括
 *
 * @author xx
 * @date 2021-12-16 15:26:42
 */
@Data
@TableName("generalization_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "中心概括")
public class GeneralizationInfo extends BaseModel<GeneralizationInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    @Size(max = 255, message = "标题超出长度255的限制")
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
}
