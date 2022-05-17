package com.px.modulars.replace.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplaceVo {
    private Integer replaceBuy;
    private String instructions;
    private LocalDateTime endTime;
    private Integer type;
    private Long userId;

    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "经度")
    private String lng;
    @ApiModelProperty(value = "纬度")
    private String lat;
}
