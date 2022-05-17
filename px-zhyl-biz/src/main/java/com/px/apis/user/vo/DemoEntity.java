package com.px.apis.user.vo;

import com.px.basic.alone.validation.rule.ProhibitedWords;
import lombok.Data;

/**
 * @author zhouz
 * @Date 2021/6/18 15:24
 * @Description
 */
@Data
public class DemoEntity {
    @ProhibitedWords(value = "demo_name", keys = "def", message = "姓名中不能包含违禁词", isReplace = true)
    private String name;

}
