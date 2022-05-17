
package com.px.modulars.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.basic.alone.cache.core.PxCacheable;
import com.px.modulars.demo.entity.DemoInfo;
import com.px.modulars.demo.mapper.DemoInfoMapper;
import com.px.modulars.demo.service.DemoInfoService;
import com.px.basic.alone.core.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 测试
 *
 * @author 吕郭飞
 * @date 2021-05-20 11:08:52
 */
@Service
public class DemoInfoServiceImpl extends BaseServiceImpl<DemoInfo, DemoInfoMapper> implements DemoInfoService {

    @Override
    @PxCacheable(region = "demoInfo", key = "#id")
    public DemoInfo getById(Serializable id) {
        return super.getById(id);
    }
}
