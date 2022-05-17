

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
 * 安全配置
 *
 * @author 吕郭飞
 * @date 2021-07-06 17:48:11
 */
@Data
@TableName("sys_policy_security")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "安全配置")
public class SysPolicySecurity extends BaseModel<SysPolicySecurity> {
    private static final long serialVersionUID = 1L;

    /**
     * 第一次进入是否修改密码，1：是，2：否
     */
    @NotNull(message = "第一次进入是否修改密码，1：是，2：否不能为空")
    @Size(max = 1, message = "第一次进入是否修改密码，1：是，2：否超出长度1的限制")
    @ApiModelProperty(value = "第一次进入是否修改密码，1：是，2：否")
    private Integer firstModify;
    /**
     * 是否定时修改密码，1：是，2：否
     */
    @NotNull(message = "是否定时修改密码，1：是，2：否不能为空")
    @Size(max = 1, message = "是否定时修改密码，1：是，2：否超出长度1的限制")
    @ApiModelProperty(value = "是否定时修改密码，1：是，2：否")
    private Integer timingModify;
    /**
     * 定时修改时间（天）
     */
    @NotNull(message = "定时修改时间（天）不能为空")
    @Size(max = 6, message = "定时修改时间（天）超出长度6的限制")
    @ApiModelProperty(value = "定时修改时间（天）")
    private Integer timingNum;
    /**
     * 验证码策略，1：不启用，2：必填，3：密码输入错误后填写
     */
    @NotNull(message = "验证码策略，1：不启用，2：必填，3：密码输入错误后填写不能为空")
    @Size(max = 1, message = "验证码策略，1：不启用，2：必填，3：密码输入错误后填写超出长度1的限制")
    @ApiModelProperty(value = "验证码策略，1：不启用，2：必填，3：密码输入错误后填写")
    private Integer verificationCodePolicy;
    /**
     * 验证码位数
     */
    @Size(max = 2, message = "验证码位数超出长度2的限制")
    @ApiModelProperty(value = "验证码位数")
    private Integer verificationCodeLen;
    /**
     * 验证码字典
     */
    @Size(max = 1000, message = "验证码字典超出长度1000的限制")
    @ApiModelProperty(value = "验证码字典")
    private String verificationCodeDice;

    @Size(max = 5, message = "验证码超时时间")
    @ApiModelProperty(value = "验证码超时时间（秒）")
    private Integer verificationCodeTimeout;
    /**
     * 密码错误次数
     */
    @Size(max = 3, message = "密码错误次数超出长度3的限制")
    @ApiModelProperty(value = "密码错误次数")
    private Integer passErrorNumCode;

    @Size(max = 3, message = "密码错误次数限定时间长度超出3位限制")
    @ApiModelProperty(value = "密码错误次数限定时间（秒）")
    private Integer passErrorNumTime;

    /**
     * 密码输入错误N次后锁定，0不锁定
     */
    @Size(max = 3, message = "密码输入错误N次后锁定，0不锁定超出长度3的限制")
    @ApiModelProperty(value = "密码输入错误N次后锁定，0不锁定")
    private Integer lockingErrorNum;

    @Size(max = 3, message = "密码错误次数限定时间长度超出3位限制")
    @ApiModelProperty(value = "密码错误次数限定时间（秒）")
    private Integer lockingErrorNumTime;

    /**
     * 锁定时间（分钟）
     */
    @Size(max = 8, message = "锁定时间（分钟）超出长度8的限制")
    @ApiModelProperty(value = "锁定时间（分钟）")
    private Integer lockingErrorTime;
}
