
package com.px.modulars.upms.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.modulars.upms.entity.SysApp;
import com.px.modulars.upms.entity.SysAppVersion;
import com.px.modulars.upms.service.SysAppService;
import com.px.modulars.upms.service.SysAppVersionService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import net.oschina.j2cache.CacheChannel;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.Cache;


/**
 * 系统APP版本日志
 *
 * @author px code generator
 * @date 2021-06-25 14:51:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/upms/sysappversion")
@Api(value = "sysappversion", tags = "系统APP版本日志管理")
public class SysAppVersionController extends BaseController<SysAppVersion,SysAppVersionService> {

    private final  SysAppVersionService sysAppVersionService;
    private final SysAppService appService;
    private final CacheChannel cacheChannel;

    private static final String region="SYSTEM_APPLICATION_VERSION";

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysAppVersion 系统APP版本日志
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('upms_sysappversion_get')")
    public R getSysAppVersionPage(Page page, SysAppVersion sysAppVersion) {
        return R.ok(
                sysAppVersionService.lambdaQuery()
                        .like(StrUtil.isNotEmpty(sysAppVersion.getContent()), SysAppVersion::getContent,sysAppVersion.getContent())
                        .eq(sysAppVersion.getAid()!=null,SysAppVersion::getAid,sysAppVersion.getAid())
                        .orderByDesc(SysAppVersion::getCreateTime)
                        .page(page)
        );
    }


    /**
     * 通过id查询系统APP版本日志
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('upms_sysappversion_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增系统APP版本日志
     * @param sysAppVersion 系统APP版本日志
     * @return R
     */
    @ApiOperation(value = "新增系统APP版本日志", notes = "新增系统APP版本日志")
    @PostMapping
    @SysLog("新增系统APP版本日志")
    @PreAuthorize("@pms.hasPermission('upms_sysappversion_add')")
    public R save(@Validated @RequestBody SysAppVersion sysAppVersion) {
        updateApp(sysAppVersion);
        return super.update(sysAppVersion, SecurityUtils.getUser().getId());
    }
    public void updateApp(SysAppVersion sysAppVersion){
        String key=appService.getById(sysAppVersion.getAid()).getAppkey();
        appService.lambdaUpdate()
                .eq(SysApp::getId,sysAppVersion.getAid())
                .set(SysApp::getVersion,sysAppVersion.getVersion())
                .set(SysApp::getContent,sysAppVersion.getContent())
                .update();
        cacheChannel.evict(region,key);
    }

    /**
     * 修改系统APP版本日志
     * @param sysAppVersion 系统APP版本日志
     * @return R
     */
    @ApiOperation(value = "修改系统APP版本日志", notes = "修改系统APP版本日志")
    @PutMapping
    @SysLog("修改系统APP版本日志")
    @PreAuthorize("@pms.hasPermission('upms_sysappversion_edit')")
    public R updateById(@Validated @RequestBody SysAppVersion sysAppVersion) {
        Integer c=sysAppVersionService.lambdaQuery()
                .eq(SysAppVersion::getAid,sysAppVersion.getAid())
                .gt(SysAppVersion::getId,sysAppVersion.getId())
                .eq(SysAppVersion::getEnable,1)
                .count();
        if(c==0){
            updateApp(sysAppVersion);
        }
        return super.update(sysAppVersion, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除系统APP版本日志
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统APP版本日志", notes = "通过id删除系统APP版本日志")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除系统APP版本日志")
    @PreAuthorize("@pms.hasPermission('upms_sysappversion_del')")
    public R removeById(@PathVariable Integer id) {
        SysAppVersion sysAppVersion=super.service.queryById(id);
        if (sysAppVersion ==null){
            return R.failed("ID错误");
        }
        return super.del(sysAppVersion, SecurityUtils.getUser().getId());
    }

}
