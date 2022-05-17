

package com.px.modulars.function.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.px.basic.alone.core.base.BaseModel;

import javax.validation.constraints.*;

/**
 * 功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
 *
 * @author liupan
 * @date 2021-11-24 14:53:15
 */
@Data
@TableName("function_room_class")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
public class FunctionRoomClass extends BaseModel<FunctionRoomClass> {
    private static final long serialVersionUID = 1L;

    /**
     * 功能室名字
     */
    @Size(max = 255, message = "功能室名字超出长度255的限制")
    @ApiModelProperty(value = "功能室名字")
    private String name;

    @ApiModelProperty(value = "图像")
    private String image;
}
