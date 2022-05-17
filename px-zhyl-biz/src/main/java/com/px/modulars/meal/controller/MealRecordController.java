
package com.px.modulars.meal.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.build.service.BuildingInfoService;
import com.px.modulars.meal.entity.MealRecord;
import com.px.modulars.meal.service.MealRecordService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.modulars.meal.vo.MealRecordVo;
import com.px.modulars.userInfo.entity.OldManInfo;
import com.px.modulars.userInfo.service.OldManInfoService;
import com.px.plugins.conversion.excel.util.ExcelBaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 订餐记录
 *
 * @author px code generator
 * @date 2021-12-03 14:53:38
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/mealRecord/mealrecord")
@Api(value = "mealrecord", tags = "订餐记录管理")
public class MealRecordController extends BaseController<MealRecord, MealRecordService> {

    private final MealRecordService mealRecordService;
    private final BuildingInfoService buildingInfoService;
    private final OldManInfoService oldManInfoService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param mealRecord 订餐记录
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('mealRecord_mealrecord_get')")
    public R getMealRecordPage(Page page, MealRecord mealRecord) {
        Page<MealRecord> mealRecordPage = mealRecordService.lambdaQuery().eq(MealRecord::getEnable, Constants.ENABLE_TRUE)
                .like(StringUtils.isNotEmpty(mealRecord.getName()), MealRecord::getName, mealRecord.getName())
                .like(StringUtils.isNotEmpty(mealRecord.getMealNumber()), MealRecord::getMealNumber, mealRecord.getMealNumber())
                .eq(mealRecord.getBuildingInfo() != null, MealRecord::getBuildingInfo, mealRecord.getBuildingInfo())
                .like(StringUtils.isNotEmpty(mealRecord.getPhone()), MealRecord::getPhone, mealRecord.getPhone())
                .eq(mealRecord.getState() != null, MealRecord::getState, mealRecord.getState())
                .orderByDesc(MealRecord::getCreateTime)
                .page(page);
        mealRecordPage.getRecords().forEach(mealRecord1 -> {
            BuildingInfo buildingInfo = buildingInfoService.getById(mealRecord1.getBuildingInfo());
            OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).eq(OldManInfo::getId, mealRecord1.getOldMan()).one();
            if(mealRecord1.getAddress()==null||"".equals(mealRecord1.getAddress())){
                mealRecord1.setBuildingInfoName(buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber()+"-"+oldManInfo.getAddress());
            }else {
                mealRecord1.setBuildingInfoName(mealRecord1.getAddress());
            }
            if (mealRecord1.getState() == 1) {
                mealRecord1.setStateName("已完成");
            } else {
                mealRecord1.setStateName("未完成");
            }
        });
        return R.ok(mealRecordPage);
    }


    /**
     * 通过id查询订餐记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('mealRecord_mealrecord_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增订餐记录
     *
     * @param mealRecord 订餐记录
     * @return R
     */
    @ApiOperation(value = "新增订餐记录", notes = "新增订餐记录")
    @PostMapping
    @SysLog("新增订餐记录")
    @PreAuthorize("@pms.hasPermission('mealRecord_mealrecord_add')")
    public R save(@RequestBody MealRecord mealRecord) {
        return super.update(mealRecord, SecurityUtils.getUser().getId());
    }

    /**
     * 修改订餐记录
     *
     * @param mealRecord 订餐记录
     * @return R
     */
    @ApiOperation(value = "修改订餐记录", notes = "修改订餐记录")
    @PutMapping
    @SysLog("修改订餐记录")
    @PreAuthorize("@pms.hasPermission('mealRecord_mealrecord_edit')")
    public R updateById(@RequestBody MealRecord mealRecord) {
        ThreadUtil.execute(() -> {
            mealRecordService.mealMsg(mealRecord.getId());
        });
        return super.update(mealRecord, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除订餐记录
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除订餐记录", notes = "通过id删除订餐记录")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除订餐记录")
    @PreAuthorize("@pms.hasPermission('mealRecord_mealrecord_del')")
    public R removeById(@PathVariable Integer id) {
        MealRecord mealRecord = super.service.queryById(id);
        if (mealRecord == null) {
            return R.failed("ID错误");
        }
        return super.del(mealRecord, SecurityUtils.getUser().getId());
    }

    /**
     * 订餐记录导出
     */
    @ApiOperation(value = "订餐记录", notes = "订餐记录")
    @GetMapping("/list/excel")
    @Inner(value = false)
    public void mealExcel(@RequestParam(required = false) String mealNumber, @RequestParam(required = false) String name,
                          @RequestParam(required = false) String phone, @RequestParam(required = false) Integer state,
                          @RequestParam(required = false) Integer type,
                          HttpServletResponse response) {
        List<MealRecordVo> mealRecordVoList = new ArrayList<>();

        List<MealRecord> mealRecordList = mealRecordService.lambdaQuery().eq(MealRecord::getEnable, Constants.ENABLE_TRUE)
                .like(StringUtils.isNotEmpty(name), MealRecord::getName, name)
                .like(StringUtils.isNotEmpty(mealNumber), MealRecord::getMealNumber, mealNumber)
                .like(StringUtils.isNotEmpty(phone), MealRecord::getPhone, phone)
                .eq(state != null, MealRecord::getState, state)
                .orderByDesc(MealRecord::getCreateTime).list();
        mealRecordList.forEach(mealRecord1 -> {
            MealRecordVo mealRecordVo = new MealRecordVo();
            BuildingInfo buildingInfo = buildingInfoService.getById(mealRecord1.getBuildingInfo());
            OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).eq(OldManInfo::getId, mealRecord1.getOldMan()).one();
            if(mealRecord1.getAddress()==null||"".equals(mealRecord1.getAddress())){
                mealRecord1.setBuildingInfoName(buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber()+"-"+oldManInfo.getAddress());
            }else {
                mealRecord1.setBuildingInfoName(mealRecord1.getAddress());
            }
            if (mealRecord1.getState() == 1) {
                mealRecord1.setStateName("已完成");
            } else {
                mealRecord1.setStateName("未完成");
            }
            BeanUtil.copyProperties(mealRecord1, mealRecordVo, true);
            //格式化时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = mealRecord1.getCreateTime().format(formatter);
            mealRecordVo.setTime(formattedDateTime);
            mealRecordVoList.add(mealRecordVo);
        });

        ExcelBaseUtil.exportExcel(mealRecordVoList, "点餐记录", "点餐记录", MealRecordVo.class, "点餐记录.xls", response);
    }
}
