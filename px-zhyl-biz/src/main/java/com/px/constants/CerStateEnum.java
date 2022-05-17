package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum CerStateEnum implements BaseEnum {

    CER_STATE_ENUM_ONE(1, "待认证"),
    CER_STATE_ENUM_TWO(2, "认证通过"),
    CER_STATE_ENUM_THREE(3, "认证不通过"),
    CER_STATE_ENUM_FOUR(4, "取消绑定");

    private Integer value;
    private String desc;

    private CerStateEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
