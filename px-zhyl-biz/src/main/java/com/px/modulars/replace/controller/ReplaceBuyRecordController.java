
package com.px.modulars.replace.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.annotation.Inner;
import com.px.modulars.build.entity.BuildingInfo;
import com.px.modulars.build.service.BuildingInfoService;
import com.px.modulars.replace.entity.ReplaceBuy;
import com.px.modulars.replace.entity.ReplaceBuyRecord;
import com.px.modulars.replace.service.ReplaceBuyRecordService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.modulars.replace.service.ReplaceBuyService;
import com.px.modulars.replace.vo.ReplaceExcelVo;
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
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 代买记录表
 *
 * @author px code generator
 * @date 2021-12-03 10:44:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/replaceBuyRecord/replacebuyrecord")
@Api(value = "replacebuyrecord", tags = "代买记录表管理")
public class ReplaceBuyRecordController extends BaseController<ReplaceBuyRecord, ReplaceBuyRecordService> {

    private final ReplaceBuyRecordService replaceBuyRecordService;
    private final ReplaceBuyService replaceBuyService;
    private final BuildingInfoService buildingInfoService;
    private final OldManInfoService oldManInfoService;

    /**
     * 分页查询
     *
     * @param page             分页对象
     * @param replaceBuyRecord 代买记录表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('replaceBuyRecord_replacebuyrecord_get')")
    public R getReplaceBuyRecordPage(Page page, ReplaceBuyRecord replaceBuyRecord) {

        Page<ReplaceBuyRecord> replaceBuyRecordPage = replaceBuyRecordService.lambdaQuery()
                .eq(replaceBuyRecord.getReplaceBuy() != null, ReplaceBuyRecord::getReplaceBuy, replaceBuyRecord.getReplaceBuy())
                .like(StringUtils.isNotEmpty(replaceBuyRecord.getOldName()), ReplaceBuyRecord::getOldName, replaceBuyRecord.getOldName())
                .eq(replaceBuyRecord.getState() != null, ReplaceBuyRecord::getState, replaceBuyRecord.getState())
                .orderByDesc(ReplaceBuyRecord::getCreateTime).page(page);
        replaceBuyRecordPage.getRecords().forEach(replaceBuyRecord1 -> {
            //代买类型
            ReplaceBuy replaceBuy = replaceBuyService.getById(replaceBuyRecord1.getReplaceBuy());
            replaceBuyRecord1.setReplaceBuyName(replaceBuy.getName());
            //楼宇信息
            BuildingInfo buildingInfo = buildingInfoService.getById(replaceBuyRecord1.getBuildingInfo());
            OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).eq(OldManInfo::getId, replaceBuyRecord1.getOldId()).one();
            if (replaceBuyRecord1.getAddress() == null || "".equals(replaceBuyRecord1.getAddress())) {
                replaceBuyRecord1.setBuildingInfoName(buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber() + "-" + oldManInfo.getAddress());
            } else {
                replaceBuyRecord1.setBuildingInfoName(replaceBuyRecord1.getAddress());
            }
            if (replaceBuyRecord1.getState() == 1) {
                replaceBuyRecord1.setStateName("已完成");
            } else {
                replaceBuyRecord1.setStateName("未完成");
            }
        });
        return R.ok(replaceBuyRecordPage);
    }


    /**
     * 通过id查询代买记录表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('replaceBuyRecord_replacebuyrecord_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增代买记录表
     *
     * @param replaceBuyRecord 代买记录表
     * @return R
     */
    @ApiOperation(value = "新增代买记录表", notes = "新增代买记录表")
    @PostMapping
    @SysLog("新增代买记录表")
    @PreAuthorize("@pms.hasPermission('replaceBuyRecord_replacebuyrecord_add')")
    public R save(@RequestBody ReplaceBuyRecord replaceBuyRecord) {
        return super.update(replaceBuyRecord, SecurityUtils.getUser().getId());
    }

    /**
     * 修改代买记录表
     *
     * @param replaceBuyRecord 代买记录表
     * @return R
     */
    @ApiOperation(value = "修改代买记录表", notes = "修改代买记录表")
    @PutMapping
    @SysLog("修改代买记录表")
    @PreAuthorize("@pms.hasPermission('replaceBuyRecord_replacebuyrecord_edit')")
    public R updateById(@RequestBody ReplaceBuyRecord replaceBuyRecord) {
        ThreadUtil.execute(() -> {
            replaceBuyRecordService.ReplaceBuyRecordMsg(replaceBuyRecord.getId());
        });
        if (replaceBuyRecord.getState() == 2) {
            replaceBuyRecord.setState(1);
        }
        if (replaceBuyRecord.getCost() == null) {
            replaceBuyRecord.setCost(new BigDecimal(0));
        }
        return super.update(replaceBuyRecord, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除代买记录表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除代买记录表", notes = "通过id删除代买记录表")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除代买记录表")
    @PreAuthorize("@pms.hasPermission('replaceBuyRecord_replacebuyrecord_del')")
    public R removeById(@PathVariable Integer id) {
        ReplaceBuyRecord replaceBuyRecord = super.service.queryById(id);
        if (replaceBuyRecord == null) {
            return R.failed("ID错误");
        }
        return super.del(replaceBuyRecord, SecurityUtils.getUser().getId());
    }


    /**
     * 代买代办记录导出
     */
    @ApiOperation(value = "代买代办记录导出", notes = "代买代办记录导出")
    @GetMapping("/list/excel")
    @Inner(value = false)
    public void execle(@RequestParam(required = false) Integer replaceBuyId, @RequestParam(required = false) String oldName,
                       @RequestParam(required = false) Integer state, @RequestParam(required = false) Integer type, HttpServletResponse response) {

        List<ReplaceExcelVo> replaceExcelVoList = new ArrayList<>();
        List<ReplaceBuyRecord> replaceBuyRecordList = replaceBuyRecordService.lambdaQuery()
                .eq(replaceBuyId != null, ReplaceBuyRecord::getReplaceBuy, replaceBuyId)
                .like(StringUtils.isNotEmpty(oldName), ReplaceBuyRecord::getOldName, oldName)
                .eq(state != null, ReplaceBuyRecord::getState, state)
                .orderByDesc(ReplaceBuyRecord::getCreateTime).list();
        replaceBuyRecordList.forEach(replaceBuyRecord1 -> {
            ReplaceExcelVo replaceExcelVo = new ReplaceExcelVo();
            //代买类型
            ReplaceBuy replaceBuy = replaceBuyService.getById(replaceBuyRecord1.getReplaceBuy());
            replaceBuyRecord1.setReplaceBuyName(replaceBuy.getName());
            //楼宇信息
            BuildingInfo buildingInfo = buildingInfoService.getById(replaceBuyRecord1.getBuildingInfo());
            OldManInfo oldManInfo = oldManInfoService.lambdaQuery().eq(OldManInfo::getEnable, Constants.ENABLE_TRUE).eq(OldManInfo::getId, replaceBuyRecord1.getOldId()).one();
            if (replaceBuyRecord1.getAddress() == null || "".equals(replaceBuyRecord1.getAddress())) {
                replaceBuyRecord1.setBuildingInfoName(buildingInfo.getCommunity() + "-" + buildingInfo.getBuildingNumber() + "-" + oldManInfo.getAddress());
            } else {
                replaceBuyRecord1.setBuildingInfoName(replaceBuyRecord1.getAddress());
            }
            if (replaceBuyRecord1.getState() == 1) {
                replaceBuyRecord1.setStateName("已完成");
            } else {
                replaceBuyRecord1.setStateName("未完成");
            }
            BeanUtil.copyProperties(replaceBuyRecord1, replaceExcelVo, true);
            //格式化时间
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String eDateTime = replaceBuyRecord1.getEndTime().format(formatter);
            replaceExcelVo.setEndTimedate(eDateTime);
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String cDateTime = replaceBuyRecord1.getCreateTime().format(formatter1);
            replaceExcelVo.setCreateTimedate(cDateTime);
            replaceExcelVoList.add(replaceExcelVo);
        });

        ExcelBaseUtil.exportExcel(replaceExcelVoList, "代买代办记录", "代买代办记录", ReplaceExcelVo.class, "代买代办记录.xls", response);

    }
}
