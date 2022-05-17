

package com.px.modulars.userInfo.mapper;

import com.px.modulars.userInfo.entity.CertificationInfo;
import org.apache.ibatis.annotations.Mapper;
import com.px.basic.alone.core.base.BaseMapperImpl;
import org.apache.ibatis.annotations.Param;

/**
 * 认证信息表
 *
 * @author px code generator
 * @date 2021-12-02 15:32:44
 */
@Mapper
public interface CertificationInfoMapper extends BaseMapperImpl<CertificationInfo> {

    /**
     * 修改认证状态
     */
    int updateCertificationState(@Param("id") Integer id,@Param("enable") Integer enable,@Param("state") Integer state);
}
