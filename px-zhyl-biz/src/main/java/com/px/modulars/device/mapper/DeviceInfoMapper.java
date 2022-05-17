

package com.px.modulars.device.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.callLocation.entity.MealEcordTableInfo;
import com.px.modulars.callLocation.entity.SosMessageInfo;
import com.px.modulars.device.entity.DeviceInfo;
import com.px.modulars.device.vo.AccountedEntity;
import com.px.modulars.device.vo.CurveEntity;
import com.px.modulars.userInfo.entity.OldManInfo;
import org.apache.ibatis.annotations.Mapper;
import com.px.basic.alone.core.base.BaseMapperImpl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备信息
 *
 * @author px code generator
 * @date 2021-12-02 13:34:52
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapperImpl<DeviceInfo> {
    /*List<OldManInfo> selectOldManList(OldManInfo oldManInfo);*/

    Page<OldManInfo> selectOldManList(Page<OldManInfo> page, @Param("oldManInfo") OldManInfo oldManInfo);

    List<DeviceInfo> selectDeviceInfoById(DeviceInfo deviceInfo);

    List<BuildingInfo> selectBuildingList(BuildingInfo buildingInfo);

    //修改设备状态
    int updateDeviceStatus(DeviceInfo deviceInfo);

    //拿到所有设备信息
    List<DeviceInfo> selectDeviceInfo();

    //根据设备编号查询老人ID
    DeviceInfo selectDeviceInfoByOldId(DeviceInfo deviceInfo);

    //查询设备编号唯一
    DeviceInfo selectDeviceInfoByDeviceNumber(DeviceInfo deviceInfo);

    //查询ID
    DeviceInfo deviceInfoById(Integer id);

    //拿到没有绑定设备的老人信息进行绑定
    List<OldManInfo> selectOldManInfo();

    //添加SOS消息
    int saveSosMessageInfo(SosMessageInfo sosMessageInfo);

    //解绑
    int contactBinding(@Param("deviceId") Integer deviceId);

    //曲线-订餐信息
    List<CurveEntity> getTogetherfor();

    //曲线-订餐信息
    List<CurveEntity> getAgents();

    //饼状图-楼宇分布,0全部，1本月，2上个月，3本季度数据，4本年数据
    List<AccountedEntity> getDistribution(@Param("timeType")Integer timeType);

    //饼状图-年龄占比,0全部，1本月，2上个月，3本季度数据，4本年数据
    List<AccountedEntity> getAgeInfo(@Param("timeType")Integer timeType);

    //柱状-年龄占比,0全部，1本月，2上个月，3本季度数据，4本年数据
    List<AccountedEntity> getAgeInfoz(@Param("timeType")Integer timeType);
}
