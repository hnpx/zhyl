

package com.px.modulars.userInfo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.px.basic.alone.core.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 认证信息表
 *
 * @author px code generator
 * @date 2021-12-02 15:32:44
 */
@Data
public class CertificationVo  {


    @ApiModelProperty(value = "姓名")
    private String name;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 与老人关系
     */
    @ApiModelProperty(value = "与老人关系")
    private String relationship;
    /**
     * 老人姓名
     */
    @ApiModelProperty(value = "老人姓名")
    private String oldName;
    /**
     * 老人手机号
     */
    @ApiModelProperty(value = "老人手机号")
    private String oldPhone;
    /**
     * 老人身份证号
     */
    @ApiModelProperty(value = "老人身份证号")
    private String oldCard;


}
