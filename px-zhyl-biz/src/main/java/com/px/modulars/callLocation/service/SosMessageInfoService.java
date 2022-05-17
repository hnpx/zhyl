package com.px.modulars.callLocation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.callLocation.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 呼叫定位SOS信息
 *
 * @author px code generator
 * @date 2021-12-06 10:41:33
 */
public interface SosMessageInfoService extends IService<SosMessageInfo>, BaseService<SosMessageInfo> {

    /**
     * 实时定位-SOS呼叫信息
     */
    Page<SosUserInfo> selectSosUserInfoList(Page<MealEcordTableInfo> page,String nameOrPhone);

    /**
     * 实时定位-SOS呼叫信息单查
     */
    List<SosUserInfo> selectSosUserInfoListBy(String nameOrPhone);

    /**
     * 实时定位-老人详情
     */
    List<UserDetailInfo> selectUserDetailsLocusList();

    /**
     * 实时定位-老人详情单独查询返回
     */
    List<UserDetailInfo> selectUserDetailsLocusListBy(UserDetailInfo userDetailInfo);

    /**
     * 实时定位-代买代办
     */
    /*List<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoList();*/
    Page<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoList(Page<ReplaceBuyRecordBDInfo> page);

    /**
     * 实时定位-代买代办单独查询返回
     */
    List<ReplaceBuyRecordBDInfo> selectReplaceBuyRecordBDInfoListBy(ReplaceBuyRecordBDInfo replaceBuyRecordBDInfo);

    /**
     * 实时定位-送餐信息
     */
    Page<MealEcordTableInfo> selectMealEcordTableInfoList(Page<MealEcordTableInfo> page,Integer type);

    /**
     * 实时定位-送餐信息单独查询返回
     */
    List<MealEcordTableInfo> selectMealEcordTableInfoListBy(MealEcordTableInfo mealEcordTableInfo);

    /**
     * 实时定位-代买代办-已完成状态修改
     */
    int updateReplaceBuyRecordState(Integer replaceBuyRecordId);

    /**
     * 实时定位-送餐信息-已完成状态修改
     */
    int updateMealRecordState(Integer mealRecordId);

    /**
     *  实时定位-SOS呼叫信息-已完成状态修改
     */
    int updateSosMessageState(Integer sosMessageId);

    //数量
    Map<String, Object> getQuantityVoMap();
}
