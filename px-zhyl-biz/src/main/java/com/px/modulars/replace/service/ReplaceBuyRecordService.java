package com.px.modulars.replace.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.vo.ReplaceVo;

import java.util.Map;

/**
 * 代买记录表
 *
 * @author px code generator
 * @date 2021-12-03 10:44:11
 */
public interface ReplaceBuyRecordService extends IService<ReplaceBuyRecord>, BaseService<ReplaceBuyRecord> {

    public Map<String,Object> ReplaceBuyRecord(ReplaceVo replaceVo);

    /**
     * 设备带代买代办接口
     * @param userId
     * @return
     */
    public Map<String,Object> EquipmentReplaceBuyRecord(Integer userId);

    public void ReplaceBuyRecordMsg(Integer id);


}
