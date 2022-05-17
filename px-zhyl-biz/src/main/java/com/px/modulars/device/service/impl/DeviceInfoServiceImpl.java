package com.px.modulars.device.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.callLocation.entity.MealEcordTableInfo;
import com.px.modulars.callLocation.entity.SosMessageInfo;
import com.px.modulars.device.entity.DeviceInfo;
import com.px.modulars.device.mapper.DeviceInfoMapper;
import com.px.modulars.device.service.DeviceInfoService;
import com.px.basic.alone.core.base.BaseServiceImpl;
import com.px.modulars.device.vo.AccountedEntity;
import com.px.modulars.device.vo.CurveEntity;
import com.px.modulars.meal.entity.MealRecord;
import com.px.modulars.meal.service.MealRecordService;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.modulars.replace.service.ReplaceBuyService;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.service.CertificationInfoService;
import com.px.modulars.userInfo.service.OldManInfoService;
import com.px.msg.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备信息
 *
 * @author px code generator
 * @date 2021-12-02 13:34:52
 */
@Service
public class DeviceInfoServiceImpl extends BaseServiceImpl<DeviceInfo, DeviceInfoMapper> implements DeviceInfoService {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;


    //代买代办数量
    @Autowired
    private ReplaceBuyRecordService replaceBuyRecordService;

    //老人数量
    @Autowired
    private OldManInfoService oldManInfoService;

    //设备数量
    @Autowired
    private DeviceInfoService deviceInfoService;

    //餐饮数量
    @Autowired
    private MealRecordService mealRecordService;

 /*   @Override
    public List<OldManInfo> selectOldManList(OldManInfo oldManInfo) {
        return deviceInfoMapper.selectOldManList(oldManInfo);
    }
*/
    @Override
    public Page<OldManInfo> selectOldManList(Page<OldManInfo> page,OldManInfo oldManInfo) {
        return deviceInfoMapper.selectOldManList(page,oldManInfo);
    }

    @Override
    public List<DeviceInfo> selectDeviceInfoById(DeviceInfo deviceInfo) {
        return deviceInfoMapper.selectDeviceInfoById(deviceInfo);
    }

    @Override
    public List<BuildingInfo> selectBuildingList(BuildingInfo buildingInfo) {
        return deviceInfoMapper.selectBuildingList(buildingInfo);
    }

    @Override
    public int updateDeviceStatus(DeviceInfo deviceInfo) {
        return deviceInfoMapper.updateDeviceStatus(deviceInfo);
    }

    @Override
    public List<DeviceInfo> selectDeviceInfo() {
        return deviceInfoMapper.selectDeviceInfo();
    }

    @Override
    public DeviceInfo selectDeviceInfoByOldId(DeviceInfo deviceInfo) {
        return deviceInfoMapper.selectDeviceInfoByOldId(deviceInfo);
    }

    @Override
    public DeviceInfo selectDeviceInfoByDeviceNumber(DeviceInfo deviceInfo) {
        return deviceInfoMapper.selectDeviceInfoByDeviceNumber(deviceInfo);
    }

    @Override
    public DeviceInfo deviceInfoById(Integer id) {
        return deviceInfoMapper.deviceInfoById(id);
    }

    @Override
    public List<OldManInfo> selectOldManInfo() {
        return deviceInfoMapper.selectOldManInfo();
    }

    @Override
    public int saveSosMessageInfo(SosMessageInfo sosMessageInfo) {
        return deviceInfoMapper.saveSosMessageInfo(sosMessageInfo);
    }

    @Override
    public int contactBinding(Integer deviceId) {
        return deviceInfoMapper.contactBinding(deviceId);
    }

    @Override
    public Map<String, Object> getOverviewMap() {
        Map<String, Object> map = new HashMap<>();
        int oldManInfoCount = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).count();
        map.put("oldManInfoCount", oldManInfoCount);
        int replaceBuyRecordCount = replaceBuyRecordService.lambdaQuery().eq(ReplaceBuyRecord::getEnable, Constants.ENABLE_TRUE).count();
        map.put("replaceBuyRecordCount", replaceBuyRecordCount);
        int deviceInfoCount = deviceInfoService.lambdaQuery().eq(DeviceInfo::getEnable, Constants.ENABLE_TRUE).count();
        map.put("deviceInfoCount", deviceInfoCount);
        int mealRecordCount = mealRecordService.lambdaQuery().eq(MealRecord::getEnable, Constants.ENABLE_TRUE).count();
        map.put("mealRecordCount", mealRecordCount);
        return map;
    }

    @Override
    public List<CurveEntity> getTogetherfor() {
        return deviceInfoMapper.getTogetherfor();
    }

    @Override
    public List<CurveEntity> getAgents() {
        return deviceInfoMapper.getAgents();
    }

    @Override
    public List<AccountedEntity> getDistribution(Integer timeType) {
        return deviceInfoMapper.getDistribution(timeType);
    }

    @Override
    public List<AccountedEntity> getAgeInfo(Integer timeType) {
        return deviceInfoMapper.getAgeInfo(timeType);
    }

    @Override
    public List<AccountedEntity> getAgeInfoz(Integer timeType) {
        return deviceInfoMapper.getAgeInfoz(timeType);
    }
}
