package com.px.constants;

import com.pig4cloud.pig.common.core.constant.enums.BaseEnum;

/**
 *
 *
 * @author zhouz
 */
public enum UserTypeStateEnum implements BaseEnum {

    USER_TYPE_STATE_ENUM_ONE(1, "老人"),
    USER_TYPE_STATE_ENUM_TWO(2, "家人"),
    USER_TYPE_STATE_ENUM_THREE(3, "团队"),
    USER_TYPE_STATE_ENUM_FOUR(4, "群众"),
    USER_TYPE_STATE_ENUM_Five(5, "既老人又是家人");

    private Integer value;
    private String desc;

    private UserTypeStateEnum(Integer value, String desc) {
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
