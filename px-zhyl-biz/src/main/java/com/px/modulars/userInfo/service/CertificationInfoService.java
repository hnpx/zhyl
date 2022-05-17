package com.px.modulars.userInfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.userInfo.entity.CertificationInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 认证信息表
 *
 * @author px code generator
 * @date 2021-12-02 15:32:44
 */
public interface CertificationInfoService extends IService<CertificationInfo>, BaseService<CertificationInfo> {
    public void Certification(Integer id);

    /**
     * 修改认证状态
     * @param id
     * @param enable
     * @param state
     * @return
     */
    int updateCertificationState(Integer id, Integer enable,Integer state);
}
