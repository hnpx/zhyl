package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum ReplaceStateEnum implements BaseEnum {

    REPLACE_TYPE_ENUM_ONE(1, "完成"),
    REPLACE_TYPE_ENUM_TWO(2, "未完成");

    private Integer value;
    private String desc;

    private ReplaceStateEnum(Integer value, String desc) {
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
