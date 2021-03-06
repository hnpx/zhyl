package com.px.apis.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.basic.alone.security.annotation.Inner;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.common.log.annotation.SysLog;
import com.px.constants.CerStateEnum;
import com.px.constants.UserTypeStateEnum;
import com.px.core.PaBaseApiController;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.build.service.BuildingInfoService;
import com.px.modulars.callLocation.entity.SosMessageInfo;
import com.px.modulars.device.service.DeviceInfoService;
import com.px.modulars.donations.entity.DonationsInfo;
import com.px.modulars.donations.service.DonationsInfoService;
import com.px.modulars.function.entity.FunctionRoomClass;
import com.px.modulars.function.service.FunctionRoomClassService;
import com.px.modulars.meal.entity.MealInfo;
import com.px.modulars.meal.entity.MealRecord;
import com.px.modulars.meal.service.MealInfoService;
import com.px.modulars.meal.service.MealRecordService;
import com.px.modulars.opinion.entity.OpinionInfo;
import com.px.modulars.opinion.service.OpinionInfoService;
import com.px.modulars.replace.entity.ReplaceBuy;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.modulars.replace.service.ReplaceBuyService;
import com.px.modulars.replace.vo.ReplaceVo;
import com.px.modulars.upms.util.websocket.util.WebSocketServer;
import com.px.modulars.userInfo.entity.CertificationInfo;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.entity.OldPhrInfo;
import com.px.modulars.userInfo.service.CertificationInfoService;
import com.px.modulars.userInfo.service.OldManInfoService;
import com.px.modulars.userInfo.service.OldPhrInfoService;
import com.px.modulars.userInfo.vo.CertificationVo;
import com.px.modulars.userInfo.vo.WxOldManVo;
import com.px.modulars.userInfo.vo.WxOldPhrVo;
import com.px.modulars.userInfo.vo.WxUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/zhyl")
@Api(value = "user", tags = "????????????????????????????????????")
public class WxApiController extends PaBaseApiController {

    @Autowired
    private FunctionRoomClassService functionRoomClassService;
    @Autowired
    private OpinionInfoService opinionInfoService;
    @Autowired
    private OldManInfoService oldManInfoService;
    @Autowired
    private CertificationInfoService certificationInfoService;
    @Autowired
    private ReplaceBuyRecordService replaceBuyRecordService;
    @Autowired
    private MealRecordService mealRecordService;
    @Autowired
    private MealInfoService mealInfoService;
    @Autowired
    private ReplaceBuyService replaceBuyService;
    //????????????
    @Autowired
    private DonationsInfoService donationsInfoService;
    //SOS
    @Autowired
    private DeviceInfoService deviceInfoService;
    //??????
    @Autowired
    private BuildingInfoService buildingInfoService;
    //????????????
    @Autowired
    private OldPhrInfoService oldPhrInfoService;
    @Value("${selectuser.url}")
    public String url;

    /**
     * ????????????????????????????????????
     *
     * @param id
     * @return WxOldPhrVo  vo
     */
    @GetMapping("/oldphr/getOne")
    @ApiOperation("????????????????????????????????????")
    @Inner(value = false)
    public R getOldPhrGetOne(@Validated String id) {
        Map<String, Object> map = new HashMap<>();
        if (id==null||id.isEmpty()) {
            return R.failed("ID??????");
        }
        //????????????
        OldManInfo oldManInfo = null;
        WxOldPhrVo wxOldPhrVo = null;
        if (id.length() < 10) {
            oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getId, Integer.parseInt(id)).eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).one();
        }
        WxOldManVo wxOldManVo = new WxOldManVo();
        if (oldManInfo != null) {
            wxOldManVo.setName(oldManInfo.getName());
            wxOldManVo.setSex(oldManInfo.getSex() == 1 ? "???" : "???");
            wxOldManVo.setPhone(oldManInfo.getPhone());
            BuildingInfo buildingInfo = buildingInfoService.lambdaQuery().eq(BuildingInfo::getId, oldManInfo.getBuilding()).eq(BuildingInfo::getEnable, Constants.ENABLE_TRUE).one();
            wxOldManVo.setAddress(buildingInfo.getCommunity() == null ? "???" : buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber() + "-" + oldManInfo.getAddress());
            wxOldManVo.setPermanentAddress(oldManInfo.getPermanentAddress());
            wxOldManVo.setStreet(oldManInfo.getStreet());
            wxOldManVo.setAge(oldManInfo.getAge()+"");
            wxOldPhrVo = oldPhrInfoService.oldPhrInfoGetOne(Integer.parseInt(id));
        } else {
            WxUserVo wxUserVo = oldManInfoService.getUserInfo(super.getCurrentId());
            if (UserTypeStateEnum.USER_TYPE_STATE_ENUM_ONE.getValue().equals(wxUserVo.getUserType())) {
                //????????????
                oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getId, wxUserVo.getId()).eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).one();
                if (oldManInfo != null) {
                    wxOldManVo.setName(oldManInfo.getName());
                    wxOldManVo.setSex(oldManInfo.getSex() == 1 ? "???" : "???");
                    wxOldManVo.setPhone(oldManInfo.getPhone());
                    BuildingInfo buildingInfo = buildingInfoService.lambdaQuery().eq(BuildingInfo::getId, oldManInfo.getBuilding()).eq(BuildingInfo::getEnable, Constants.ENABLE_TRUE).one();
                    wxOldManVo.setAddress(buildingInfo.getCommunity() == null ? "???" : buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber() + "-" + oldManInfo.getAddress());
                    wxOldManVo.setPermanentAddress(oldManInfo.getPermanentAddress());
                    wxOldManVo.setStreet(oldManInfo.getStreet());
                    wxOldManVo.setAge(oldManInfo.getAge()+"");
                    long a = wxUserVo.getId();
                    int b = (int) a;
                    wxOldPhrVo = oldPhrInfoService.oldPhrInfoGetOne(b);
                }
            }else {
                map.put("wxOldPhrVo", null);
                map.put("wxOldManVo", null);
                return R.ok(map);
            }
        }
        map.put("wxOldPhrVo", wxOldPhrVo);
        map.put("wxOldManVo", wxOldManVo);
        return R.ok(map);
    }


    /**
     * SOS????????????
     *
     * @param sosMessageInfo ????????????????????????
     * @return R
     */
    @PostMapping("/sosMessage/add")
    @ApiOperation("SOS????????????")
    @Inner(value = false)
    public R sosMessageSave(@RequestBody SosMessageInfo sosMessageInfo) {
       /* WxUserVo wxUserVo = oldManInfoService.getUserInfo(super.getCurrentId());
        wxUserVo.getNickname();*/

        //??????SOS??????
        sosMessageInfo.setEnable(1);
        sosMessageInfo.setIsSolve("2");
        sosMessageInfo.setCreateTime(LocalDateTime.now());
        //?????????????????????
        OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getPhone, sosMessageInfo.getPhone()).eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).one();
        if (oldManInfo != null) {
            sosMessageInfo.setOldId(oldManInfo.getId());
        }
        int a = deviceInfoService.saveSosMessageInfo(sosMessageInfo);
        if (a > 0) {
            try {
                if (oldManInfo != null) {
                    WebSocketServer.BroadCastInfo("????????????" + oldManInfo.getName() + " ???????????????" + oldManInfo.getPhone() + " ?????????SOS???????????????????????????");
                } else {
                    WebSocketServer.BroadCastInfo("???????????????" + sosMessageInfo.getPhone() + " ?????????SOS???????????????????????????");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.ok();
    }

    /**
     * ??????????????????
     *
     * @param donationsInfo ????????????
     * @return R
     */
    @PostMapping("/donations/add")
    @ApiOperation("??????????????????")
    @Inner(value = false)
    public R save(@RequestBody DonationsInfo donationsInfo) {
        donationsInfo.setCreateTime(LocalDateTime.now());
        donationsInfo.setAllUserId(super.getCurrentId());
        return R.ok(donationsInfoService.save(donationsInfo));
    }

    /**
     * ??????????????????
     */
/*    @GetMapping("/donations/list")
    @ApiOperation("??????????????????")
    @Inner(value = false)
    public R getDonationsList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page page = new Page(pageNo, pageSize);
        Page<DonationsInfo> donationsInfoPage = donationsInfoService.lambdaQuery()
                .eq(DonationsInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(DonationsInfo::getAllUserId, super.getCurrentId())
                .orderByDesc(DonationsInfo::getCreateTime)
                .page(page);
        return R.ok(donationsInfoPage);
    }*/

    /**
     * ????????????????????????
     */
/*    @GetMapping("/donations/getOne")
    @ApiOperation("????????????????????????")
    @Inner(value = false)
    public R getDonationsGetOne(Integer id) {
        if (id == null) {
            return R.failed("ID??????");
        }
        DonationsInfo donationsInfo=donationsInfoService.lambdaQuery()
                .eq(DonationsInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(DonationsInfo::getAllUserId, super.getCurrentId())
                .eq(DonationsInfo::getId, id).one();
        return R.ok(donationsInfo);
    }*/


    /**
     * ??????????????????
     *
     * @return
     */
    @PostMapping("/opinion/add")
    @ApiOperation("??????????????????")
    @Inner(value = false)
    public R add(@RequestBody OpinionInfo opinionInfo) {
        opinionInfo.setCreateTime(LocalDateTime.now());

        opinionInfo.setAllUserId(super.getCurrentId());
        return R.ok(opinionInfoService.save(opinionInfo));
    }

    /**
     * ??????????????????
     */
    @GetMapping("/opinion/list")
    @ApiOperation("??????????????????")
    @Inner(value = false)
    public R getList(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        Page page = new Page(pageNo, pageSize);
        Page<OpinionInfo> opinionInfoPage = opinionInfoService.lambdaQuery().eq(OpinionInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(OpinionInfo::getAllUserId, super.getCurrentId())
                .orderByDesc(OpinionInfo::getCreateTime)
                .page(page);
        return R.ok(opinionInfoPage);
    }

    /**
     * ????????????
     */
    @PostMapping("/binding/old")
    @ApiOperation("????????????")
    @Inner(value = false)
    public R bingdingOld(@RequestBody CertificationVo certificationVo) {
        //??????????????????????????????
        int count = certificationInfoService.lambdaQuery().eq(CertificationInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(CertificationInfo::getUserId, super.getCurrentId())
                .eq(CertificationInfo::getState, CerStateEnum.CER_STATE_ENUM_TWO).count();
        if (count > 0) {
            return R.failed("??????????????????????????????????????????");
        }

        OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE)
                .eq(OldManInfo::getPhone, certificationVo.getOldPhone())
                .eq(OldManInfo::getIdCard, certificationVo.getOldCard()).one();
        if (oldManInfo == null) {
            return R.failed().setMsg("?????????????????????");
        }
        //?????????????????????????????????
        int count1 = certificationInfoService.lambdaQuery().eq(CertificationInfo::getEnable, Constants.ENABLE_TRUE).eq(CertificationInfo::getState, CerStateEnum.CER_STATE_ENUM_TWO.getValue())
                .eq(CertificationInfo::getOldManInfo, oldManInfo.getId()).count();
        if (count1 > 0) {
            return R.failed().setMsg("????????????????????????");
        }
        CertificationInfo certificationInfo = new CertificationInfo();
        BeanUtil.copyProperties(certificationVo, certificationInfo, true);
        certificationInfo.setCreateTime(LocalDateTime.now());
        certificationInfo.setOldManInfo(oldManInfo.getId());
        certificationInfo.setUserId(super.getCurrentId());
        certificationInfo.setState(CerStateEnum.CER_STATE_ENUM_ONE.getValue());
        certificationInfo.setOldName(oldManInfo.getName());
        certificationInfoService.save(certificationInfo);
        return R.ok();
    }


    /**
     * ????????????
     */
    @PostMapping("/replace/buy")
    @ApiOperation("????????????")
    @Inner(value = false)
    public R replaceBuy(@RequestBody ReplaceVo replaceVo) {
        replaceVo.setUserId(super.getCurrentId());
        Map<String, Object> map = replaceBuyRecordService.ReplaceBuyRecord(replaceVo);
        if (map.get("code").equals(1)) {
            return R.ok(map.get("msg"));
        } else {
            return R.failed().setMsg(map.get("msg").toString());
        }
    }

    /**
     * ????????????
     * type(1.????????????2.????????????)
     */
    @GetMapping("/user/getOne")
    @ApiOperation("????????????")
    @Inner(value = false)
    public R getUser(@RequestParam("type") Integer type) {
        if (type == 1) {
            WxUserVo wxUserVo = oldManInfoService.getUserInfo(super.getCurrentId());
            return R.ok(wxUserVo);
        } else {
            CertificationInfo certificationInfo = certificationInfoService.lambdaQuery().eq(CertificationInfo::getEnable, Constants.ENABLE_TRUE)
                    .eq(CertificationInfo::getUserId, super.getCurrentId()).one();
            if (certificationInfo != null) {
                OldManInfo oldManInfo = oldManInfoService.getById(certificationInfo.getOldManInfo());
                WxUserVo wxUserVo = new WxUserVo();
                wxUserVo.setPhone(oldManInfo.getPhone());
                wxUserVo.setNickname(oldManInfo.getName());
                wxUserVo.setIdCard(oldManInfo.getIdCard());
                wxUserVo.setCerState(certificationInfo.getState());
                wxUserVo.setId(Long.parseLong(certificationInfo.getOldManInfo().toString()));
                wxUserVo.setReason(certificationInfo.getReason());
                wxUserVo.setBuilding(oldManInfo.getBuilding());
                BuildingInfo buildingInfo = buildingInfoService.getById(oldManInfo.getBuilding());
                if (buildingInfo != null) {
                    wxUserVo.setAddress(buildingInfo.getAddress());
                    wxUserVo.setLat(buildingInfo.getLat());
                    wxUserVo.setLng(buildingInfo.getLng());
                }
                return R.ok(wxUserVo);
            } else {
                return R.ok();
            }
        }
    }

    /**
     * ??????
     * type(1.??????2.?????????)
     *
     * @param address ??????
     * @param lng     ??????
     * @param lat     ??????
     */
    @GetMapping("/meal/reserve")
    @ApiOperation("??????")
    public R mealReserve(@RequestParam("type") Integer type, @RequestParam("mealId") Integer mealId, String address, String lng, String lat) {
        Map<String, Object> map = mealRecordService.mealRecord(type, mealId, super.getCurrentId(), address, lng, lat);
        if (map.get("code").equals(1)) {
            return R.ok(map.get("msg"));
        } else {
//            return R.failed(map.get("msg"));
            return R.failed().setMsg(map.get("msg").toString());
        }
    }


    /**
     * ????????????
     */
    @GetMapping("/meal/record")
    @ApiOperation("????????????")
    public R mealRecord(@RequestParam("oldId") Integer oldId, @RequestParam("pageNo") Integer pageNo
            , @RequestParam("pageSize") Integer pageSize) {

        Page<MealRecord> mealRecordPage = mealRecordService.lambdaQuery().eq(MealRecord::getOldMan, oldId)
                .eq(MealRecord::getUserId, super.getCurrentId())
                .orderByDesc(MealRecord::getCreateTime)
                .page(new Page(pageNo, pageSize));
        mealRecordPage.getRecords().forEach(mealRecord -> {
            MealInfo mealInfo = mealInfoService.getById(mealRecord.getMealInfo());
            mealRecord.setMealInfoVo(mealInfo);
        });
        return R.ok(mealRecordPage);

    }

    /**
     * ????????????
     */
    @GetMapping("/buy/record")
    @ApiOperation("????????????")
    public R buyRecord(@RequestParam("oldId") Integer oldId, @RequestParam("pageNo") Integer pageNo
            , @RequestParam("pageSize") Integer pageSize) {

        Page<ReplaceBuyRecord> replaceBuyRecordPage = replaceBuyRecordService.lambdaQuery()
                .eq(ReplaceBuyRecord::getOldId, oldId)
                .eq(ReplaceBuyRecord::getUserId, super.getCurrentId())
                .orderByDesc(ReplaceBuyRecord::getCreateTime).page(new Page(pageNo, pageSize));
        replaceBuyRecordPage.getRecords().forEach(replaceBuyRecord -> {
            ReplaceBuy replaceBuy = replaceBuyService.getById(replaceBuyRecord.getReplaceBuy());
            replaceBuyRecord.setReplaceBuyVo(replaceBuy);
        });
        return R.ok(replaceBuyRecordPage);
    }
}
