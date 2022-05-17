package com.px.modulars.replace.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.px.constants.CerStateEnum;
import com.px.constants.IsEquipmentEnum;
import com.px.constants.ReplaceStateEnum;
import com.px.constants.ReplaceTypeEnum;
import com.px.modulars.meal.entity.MealInfo;
import com.px.modulars.replace.entity.ReplaceBuy;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.mapper.ReplaceBuyRecordMapper;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.basic.alone.core.base.BaseServiceImpl;
import com.px.modulars.replace.service.ReplaceBuyService;
import com.px.modulars.replace.vo.ReplaceVo;
import com.px.modulars.upms.util.websocket.util.WebSocketServer;
import com.px.modulars.userInfo.entity.CertificationInfo;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.service.CertificationInfoService;
import com.px.modulars.userInfo.service.OldManInfoService;
import com.px.modulars.userInfo.vo.WxUserVo;
import com.px.msg.IMessageService;
import com.px.msg.MessageService;
import com.px.msg.vo.SendMessageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 代买记录表
 *
 * @author px code generator
 * @date 2021-12-03 10:44:11
 */
@Service
public class ReplaceBuyRecordServiceImpl extends BaseServiceImpl<ReplaceBuyRecord, ReplaceBuyRecordMapper> implements ReplaceBuyRecordService {

    @Autowired
    private ReplaceBuyRecordService replaceBuyRecordService;
    @Autowired
    private CertificationInfoService certificationInfoService;
    @Autowired
    private OldManInfoService oldManInfoService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReplaceBuyService replaceBuyService;

    @Override
    public Map<String, Object> ReplaceBuyRecord(ReplaceVo replaceVo) {
        Map<String, Object> map = new HashMap<>();
        //判断代买类型（1.代买2.自己买）
        if (replaceVo.getType().equals(ReplaceTypeEnum.REPLACE_TYPE_ENUM_ONE.getValue())) {
            //查询绑定的老人
            CertificationInfo certificationInfo = certificationInfoService.lambdaQuery().eq(CertificationInfo::getUserId, replaceVo.getUserId())
                    .eq(CertificationInfo::getEnable, Constants.ENABLE_TRUE)
                    .eq(CertificationInfo::getState, CerStateEnum.CER_STATE_ENUM_TWO.getValue()).one();
            if (certificationInfo != null) {
                //添加代买记录
                ReplaceBuyRecord replaceBuyRecord = new ReplaceBuyRecord();
                BeanUtil.copyProperties(replaceVo, replaceBuyRecord, true);
                replaceBuyRecord.setCreateTime(LocalDateTime.now());
                replaceBuyRecord.setState(ReplaceStateEnum.REPLACE_TYPE_ENUM_TWO.getValue());
                replaceBuyRecord.setOldId(certificationInfo.getOldManInfo());
                replaceBuyRecord.setIsEquipment(IsEquipmentEnum.IS_EQUIPMENT_ENUM_TWO.getValue());
                //绑定老人信息
                try {
                    OldManInfo oldManInfo = oldManInfoService.getById(certificationInfo.getOldManInfo());
                    replaceBuyRecord.setOldName(oldManInfo.getName());
                    replaceBuyRecord.setPhone(oldManInfo.getPhone());
                    replaceBuyRecord.setBuildingInfo(oldManInfo.getBuilding());
                    if (replaceVo.getAddress() == null) {
                        //等于空的话就是拿老人所在的楼宇信息
                        replaceBuyRecord.setAddress(oldManInfo.getAddress());
                    } else {
                        //添加详细地址
                        replaceBuyRecord.setAddress(replaceVo.getAddress());
                        replaceBuyRecord.setLat(replaceVo.getLat());
                        replaceBuyRecord.setLng(replaceVo.getLng());
                    }

                } catch (Exception e) {

                }
                replaceBuyRecordService.save(replaceBuyRecord);
                map.put("code", 1);
                map.put("msg", "成功");
                try {
                    WebSocketServer.BroadCastInfo("姓名为：" + replaceBuyRecord.getOldName() + " 手机号为：" + replaceBuyRecord.getPhone() + " 有新的代买代办消息，请及时查看！");
                } catch (Exception e) {

                }
                return map;
            } else {
                map.put("code", 2);
                map.put("msg", "您还未绑定老人或者绑定老人还未通过审核");
                return map;
            }
        } else {
            ReplaceBuyRecord replaceBuyRecord = new ReplaceBuyRecord();
            BeanUtil.copyProperties(replaceVo, replaceBuyRecord, true);
            replaceBuyRecord.setCreateTime(LocalDateTime.now());
            replaceBuyRecord.setState(ReplaceStateEnum.REPLACE_TYPE_ENUM_TWO.getValue());
            //查询老人信息
            WxUserVo wxUserVo = oldManInfoService.getUserInfo(replaceVo.getUserId());
            if (wxUserVo != null) {
                if (StringUtils.isNotEmpty(wxUserVo.getPhone())) {
                    OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getPhone, wxUserVo.getPhone()).eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).one();
                    if (oldManInfo != null) {
                        replaceBuyRecord.setOldId(oldManInfo.getId());
                        replaceBuyRecord.setIsEquipment(IsEquipmentEnum.IS_EQUIPMENT_ENUM_TWO.getValue());
                        //绑定老人信息
                        try {
                            replaceBuyRecord.setOldName(oldManInfo.getName());
                            replaceBuyRecord.setPhone(oldManInfo.getPhone());
                            replaceBuyRecord.setBuildingInfo(oldManInfo.getBuilding());
                            replaceBuyRecord.setAddress(oldManInfo.getAddress());
                            if (replaceVo.getAddress() == null) {
                                //等于空的话就是拿老人所在的楼宇信息
                                replaceBuyRecord.setAddress(oldManInfo.getAddress());
                            } else {
                                //添加详细地址
                                replaceBuyRecord.setAddress(replaceVo.getAddress());
                                replaceBuyRecord.setLat(replaceVo.getLat());
                                replaceBuyRecord.setLng(replaceVo.getLng());
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        map.put("code", 2);
                        map.put("msg", "您当前身份不是老人不能进行代买代办");
                        return map;
                    }

                } else {
                    map.put("code", 2);
                    map.put("msg", "您未授权手机号");
                    return map;
                }
            }
            replaceBuyRecordService.save(replaceBuyRecord);
            map.put("code", 1);
            map.put("msg", "成功");
            try {
                WebSocketServer.BroadCastInfo("姓名为：" + replaceBuyRecord.getOldName() + " 手机号为：" + replaceBuyRecord.getPhone() + " 有新的代买代办消息，请及时查看！");
            } catch (Exception e) {

            }
            return map;
        }

    }

    @Override
    public Map<String, Object> EquipmentReplaceBuyRecord(Integer userId) {

        Map<String, Object> map = new HashMap<>();
        ReplaceBuyRecord replaceBuyRecord = new ReplaceBuyRecord();
        replaceBuyRecord.setCreateTime(LocalDateTime.now());
        replaceBuyRecord.setState(ReplaceStateEnum.REPLACE_TYPE_ENUM_TWO.getValue());
        replaceBuyRecord.setOldId(userId);
        //查询老人信息
        OldManInfo oldManInfo = oldManInfoService.getById(userId);
        replaceBuyRecord.setOldId(oldManInfo.getId());
        replaceBuyRecord.setIsEquipment(IsEquipmentEnum.IS_EQUIPMENT_ENUM_ONE.getValue());
        //绑定老人信息
        try {
            replaceBuyRecord.setOldName(oldManInfo.getName());
            replaceBuyRecord.setPhone(oldManInfo.getPhone());
            replaceBuyRecord.setBuildingInfo(oldManInfo.getBuilding());
        } catch (Exception e) {

        }
        replaceBuyRecordService.save(replaceBuyRecord);
        map.put("code", 1);
        map.put("msg", "成功");
        try {
            WebSocketServer.BroadCastInfo("姓名为：" + replaceBuyRecord.getOldName() + " 手机号为：" + replaceBuyRecord.getPhone() + " 有新的代买代办消息，请及时查看！");
        } catch (Exception e) {

        }
        return map;
    }

    @Override
    public void ReplaceBuyRecordMsg(Integer id) {
        ReplaceBuyRecord replaceBuyRecord = replaceBuyRecordService.getById(id);
        WxUserVo wxUserVo = new WxUserVo();
        if (replaceBuyRecord.getIsEquipment() == 2) {
            wxUserVo = oldManInfoService.getUserInfo(Long.parseLong(replaceBuyRecord.getUserId().toString()));
            //短信通知
       /* SendMessageParam msgParam = new SendMessageParam();
        msgParam.setMethod(IMessageService.METHOD_SMS);
        msgParam.setKey("personalAcStart");
        msgParam.putData("aname", "", 20);
        msgParam.addTo("17737469506");
        this.messageService.send(msgParam);*/
            //微信模板通知
            DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String strDate2 = dtf2.format(replaceBuyRecord.getCreateTime());
            ReplaceBuy replaceBuy = replaceBuyService.getById(replaceBuyRecord.getReplaceBuy());
            String str = replaceBuyRecord.getOldName() + "的" + replaceBuy.getName() + "需求，已送达";
            SendMessageParam msgParam1 = new SendMessageParam();
            msgParam1.setMethod(IMessageService.METHOD_WX);
            msgParam1.setKey("orderFinish");
            msgParam1.putData("thing2", str, 20);
            msgParam1.putData("thing4", "请注意及时查收");
            //msgParam1.putOtherData("id", "");
//            msgParam1.addTo("oqlqo5IPuLXlMxUjQ1d8e3wKfhtM");
            msgParam1.addTo(wxUserVo.getOpenid());
            this.messageService.send(msgParam1);
        }

    }
}
