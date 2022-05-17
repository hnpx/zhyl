package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum ReplaceTypeEnum implements BaseEnum {

    REPLACE_TYPE_ENUM_ONE(1, "代买"),
    REPLACE_TYPE_ENUM_TWO(2, "自己买");

    private Integer value;
    private String desc;

    private ReplaceTypeEnum(Integer value, String desc) {
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
