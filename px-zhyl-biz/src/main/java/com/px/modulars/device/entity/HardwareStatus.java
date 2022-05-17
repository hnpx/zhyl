package com.px.modulars.device.entity;

import lombok.Data;

import java.util.Date;
@Data
public class HardwareStatus {
    /** 硬件编号 */
    private String hardwareCode;
    /** 0不正常-1 正常 */
    private String isNormal;
    /** 0关机-1开机 */
    private String isPoweron;
    /** 0 未通话 1 通话 */
    private String isCall;
    /** 0 未报警 1 报警 */
    private String isPolice;
    /** 0 未摔倒 1 摔倒 */
    private String isTumble;
    /** 0未闹钟 1 闹钟 */
    private String isClock;
    /** '0 关闭摔倒功能  1 开启摔倒' */
    private String ocTumble;
    /** 电量   0~100 */
    private String voltameter;
    /** 拨打电话（0：关闭  1：开启） */
    private String isDial;

    private Long checkId;

    private String setSuccess;

    private String isDelete;

    private String createdAt;

    private String updatedAt;


}