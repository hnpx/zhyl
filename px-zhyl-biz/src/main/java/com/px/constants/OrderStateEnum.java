package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum OrderStateEnum implements BaseEnum {

    ORDER_STATE_ENUM_ONE(1, "已预订"),
    ORDER_STATE_ENUM_TWO(2, "未预定");

    private Integer value;
    private String desc;

    private OrderStateEnum(Integer value, String desc) {
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
