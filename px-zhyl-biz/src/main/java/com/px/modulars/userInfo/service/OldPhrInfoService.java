package com.px.modulars.userInfo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.userInfo.entity.OldPhrInfo;
import com.px.modulars.userInfo.vo.WxOldPhrVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

/**
 * 老人健康档案
 *
 * @author XX
 * @date 2021-12-27 10:59:03
 */
public interface OldPhrInfoService extends IService<OldPhrInfo>, BaseService<OldPhrInfo> {

    Page<OldPhrInfo> oldPhrInfoPage(Page<OldPhrInfo> page, OldPhrInfo  oldPhrInfo);

    WxOldPhrVo oldPhrInfoGetOne(Integer id);

}
