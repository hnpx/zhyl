package com.px.modulars.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.basic.alone.validation.core.IProhibiteWordService;
import com.px.modulars.upms.entity.SysProhibiteWord;

/**
 * 关键词过滤
 *
 * @author 吕郭飞
 * @date 2021-06-18 15:34:16
 */
public interface SysProhibiteWordService extends IService<SysProhibiteWord>, BaseService<SysProhibiteWord> , IProhibiteWordService {

}
