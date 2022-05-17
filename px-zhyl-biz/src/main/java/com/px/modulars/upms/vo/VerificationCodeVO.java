package com.px.modulars.upms.vo;

import lombok.Data;

/**
 * 验证码信息VO
 *
 * @author zhouz
 * @Date 2021/7/7 11:18
 * @Description
 */
@Data
public class VerificationCodeVO {
    /**
     * 验证码Token
     */
    private String token;

    /**
     * 验证码
     */
    private String code;


}
