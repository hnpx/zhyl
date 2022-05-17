

package com.px.modulars.function.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.basic.alone.core.base.BaseModel;
import javax.validation.constraints.*;

/**
 * 功能室基本信息
 *
 * @author liupan
 * @date 2021-11-24 14:53:05
 */
@Data
@TableName("function_room")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "功能室基本信息")
public class FunctionRoom extends BaseModel<FunctionRoom> {
private static final long serialVersionUID = 1L;

                                                                            /**
     * 标题
     */
                        @Size(max=255,message = "标题超出长度255的限制")
                @ApiModelProperty(value="标题")
        private String title;
                /**
     * 标题图
     */
                        @Size(max=500,message = "标题图超出长度500的限制")
                @ApiModelProperty(value="标题图")
        private String image;
                /**
     * 内容
     */
                        @ApiModelProperty(value="内容")
        private String content;
                /**
     * 浏览量
     */
                        @Size(max=4,message = "浏览量超出长度4的限制")
                @ApiModelProperty(value="浏览量")
        private Integer views;
                /**
     * 房间分类
     */
                        @Size(max=11,message = "房间分类超出长度11的限制")
                @ApiModelProperty(value="房间分类")
        private Integer roomClass;
                /**
     * 捐款捐物类型（1.公益事项2.捐资捐物）
     */
                        @Size(max=2,message = "捐款捐物类型（1.公益事项2.捐资捐物）超出长度2的限制")
                @ApiModelProperty(value="捐款捐物类型（1.公益事项2.捐资捐物）")
        private Integer type;
        }
