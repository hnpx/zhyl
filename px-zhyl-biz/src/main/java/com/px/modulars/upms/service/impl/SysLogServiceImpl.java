

package com.px.modulars.upms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.px.basic.alone.core.base.BaseServiceImpl;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.common.log.entity.AbsSysLog;
import com.px.common.log.service.ISysLogService;
import com.px.modulars.upms.entity.SysLog;
import com.px.modulars.upms.mapper.SysLogMapper;
import com.px.modulars.upms.service.SysLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author pinxun
 * @since 2019/2/1
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLog, SysLogMapper> implements SysLogService, ISysLogService {

    @Override
    public void save(AbsSysLog absSysLog, String s) {
        SysLog log = new SysLog();

        BeanUtil.copyProperties(absSysLog, log);
        //获取当前登录用户
        super.save(log);
    }
}
