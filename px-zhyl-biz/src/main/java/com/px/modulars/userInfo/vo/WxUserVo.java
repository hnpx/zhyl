package com.px.modulars.userInfo.vo;

import lombok.Data;


@Data
public class WxUserVo {

    private String avatar;
    private String nickname;
    private Integer userType;
    private Long id;
    private String phone;
    private String idCard;
    private String openid;
    private Integer cerState;
    private String reason;
    private Integer building;
    private String address;
    private String lng;
    private String lat;


}
