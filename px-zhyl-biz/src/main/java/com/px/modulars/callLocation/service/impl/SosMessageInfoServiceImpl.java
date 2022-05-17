package com.px.modulars.callLocation.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.px.modulars.callLocation.entity.*;
import com.px.modulars.callLocation.mapper.SosMessageInfoMapper;
import com.px.modulars.callLocation.service.SosMessageInfoService;
import com.px.basic.alone.core.base.BaseServiceImpl;
import com.px.modulars.meal.entity.MealRecord;
import com.px.modulars.meal.service.MealRecordService;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.modulars.userInfo.entity.OldManInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 呼叫定位SOS信息
 *
 * @author px code generator
 * @date 2021-12-06 10:41:33
 */
@Service
public class SosMessageInfoServiceImpl extends BaseServiceImpl<SosMessageInfo, SosMessageInfoMapper> implements SosMessageInfoService {

    @Autowired
    private SosMessageInfoMapper sosMessageInfoMapper;


    //SOs
    @Autowired
    private SosMessageInfoService sosMessageInfoService;

    //订餐
    @Autowired
    private MealRecordService mealRecordService;

    //代买代办
    @Autowired
    private ReplaceBuyRecordService replaceBuyRecordService;

    @Override
    public Page<SosUserInfo> selectSosUserInfoList(Page<MealEcordTableInfo> page, String nameOrPhone) {
        return sosMessageInfoMapper.selectSosUserInfoList(page, nameOrPhone);
    }

    @Override
    public List<SosUserInfo> selectSosUserInfoListBy(String nameOrPhone) {
        return sosMessageInfoMapper.selectSosUserInfoListBy(nameOrPhone);
    }

    @Override
    public List<UserDetailInfo> selectUserDetailsLocusList() {
        return sosMessageInfoMapper.selectUserDetailsLocusList();
    }

    @Override
    public List<UserDetailInfo> selectUserDetailsLocusListBy(UserDetailInfo userDetailInfo) {
        return sosMessageInfoMapper.selectUserDetailsLocusListBy(userDetailInfo);
    }

    @Override
    public Page<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoList(Page<ReplaceBuyRecordBDInfo> page) {
        return sosMessageInfoMapper.selectReplaceBuyRecordBDInfoList(page);
    }

  /*  @Override
    public List<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoList() {
        return sosMessageInfoMapper.selectReplaceBuyRecordBDInfoList();
    }*/

    @Override
    public List<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoListBy(ReplaceBuyRecordBDInfo replaceBuyRecordBDInfo) {
        return sosMessageInfoMapper.selectReplaceBuyRecordBDInfoListBy(replaceBuyRecordBDInfo);
    }

    @Override
    public Page<MealEcordTableInfo> selectMealEcordTableInfoList(Page<MealEcordTableInfo> page, Integer type) {
        return sosMessageInfoMapper.selectMealEcordTableInfoList(page, type);
    }

    @Override
    public List<MealEcordTableInfo> selectMealEcordTableInfoListBy(MealEcordTableInfo mealEcordTableInfo) {
        return sosMessageInfoMapper.selectMealEcordTableInfoListBy(mealEcordTableInfo);
    }

    @Override
    public int updateReplaceBuyRecordState(Integer replaceBuyRecordId) {
        return sosMessageInfoMapper.updateReplaceBuyRecordState(replaceBuyRecordId);
    }

    @Override
    public int updateMealRecordState(Integer mealRecordId) {
        return sosMessageInfoMapper.updateMealRecordState(mealRecordId);
    }

    @Override
    public int updateSosMessageState(Integer sosMessageId) {
        return sosMessageInfoMapper.updateSosMessageState(sosMessageId);
    }


    @Override
    public Map<String, Object> getQuantityVoMap() {
        Map<String, Object> map = new HashMap<>();
        int sosMessage = sosMessageInfoService.lambdaQuery().eq(SosMessageInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(SosMessageInfo::getIsSolve, Constants.ENABLE_FALSE).count();
        map.put("sosMessage", sosMessage);
        int replaceBuyRecord = replaceBuyRecordService.lambdaQuery().eq(ReplaceBuyRecord::getEnable, Constants.ENABLE_TRUE)
                .eq(ReplaceBuyRecord::getState, Constants.ENABLE_FALSE)
                .count();
        map.put("replaceBuyRecord", replaceBuyRecord);
        Page page = new Page();
        Page<MealEcordTableInfo> mealRecord = sosMessageInfoMapper.selectMealEcordTableInfoList(page, null);
        map.put("mealRecord", mealRecord.getTotal());
        return map;
    }
}
