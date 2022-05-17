

package com.px.modulars.helpold.entity;

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
 * 智慧助老
 *
 * @author px code generator
 * @date 2021-12-01 11:24:55
 */
@Data
@TableName("help_old")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "智慧助老")
public class HelpOld extends BaseModel<HelpOld> {
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
    /**
     * 标图
     */
    @Size(max = 255, message = "标图超出长度255的限制")
    @ApiModelProperty(value = "标图")
    private String plotting;
    /**
     * 是否展示，1展示，0不展示
     */
    @Size(max = 255, message = "是否展示，1展示，0不展示超出长度255的限制")
    @ApiModelProperty(value = "是否展示，1展示，0不展示")
    private String isExhibition;
}
