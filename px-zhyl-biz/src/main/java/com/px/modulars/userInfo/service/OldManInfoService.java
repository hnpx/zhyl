package com.px.modulars.userInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.vo.WxUserVo;

/**
 * 老人基本信息
 *
 * @author px code generator
 * @date 2021-11-26 10:23:04
 */
public interface OldManInfoService extends IService<OldManInfo>, BaseService<OldManInfo> {

    public WxUserVo getUserInfo(Long uid);

    /**
     * SOS 报警通知
     * @param uid
     */
    public void sosMsg(Integer uid);

}
