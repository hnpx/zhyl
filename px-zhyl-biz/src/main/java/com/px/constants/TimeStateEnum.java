package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum TimeStateEnum implements BaseEnum {

    TIME_STATE_ENUM_ONE(1, "未开始"),
    TIME_STATE_ENUM_TWO(2, "进行中"),
    TIME_STATE_ENUM_THREE(3, "已结束");


    private Integer value;
    private String desc;

    private TimeStateEnum(Integer value, String desc) {
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
