package com.px.modulars.helpold.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.helpold.entity.HelpOld;

import java.util.ArrayList;
import java.util.List;

/**
 * 智慧助老
 *
 * @author px code generator
 * @date 2021-12-01 11:24:55
 */
public interface HelpOldService extends IService<HelpOld>, BaseService<HelpOld> {
    /**
     * 查询全部智慧助老数据
     * @return 数据
     */
    List<HelpOld> selectHelpoldList();

}
