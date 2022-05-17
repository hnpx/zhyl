

package com.px.modulars.oneTouchCall.entity;

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
 * 一键呼叫号码
 *
 * @author XX
 * @date 2021-12-10 15:14:06
 */
@Data
@TableName("one_touch_call_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "一键呼叫号码")
public class OneTouchCallInfo extends BaseModel<OneTouchCallInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 紧急呼叫号码
     */
    @Size(max = 255, message = "紧急呼叫号码超出长度255的限制")
    @ApiModelProperty(value = "紧急呼叫号码")
    private String phone;
}
