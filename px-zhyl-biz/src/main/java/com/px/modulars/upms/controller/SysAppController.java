
package com.px.modulars.upms.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.modulars.upms.entity.SysApp;
import com.px.modulars.upms.service.SysAppService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 系统APP版本
 *
 * @author px code generator
 * @date 2021-06-25 14:51:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/upms/sysapp")
@Api(value = "sysapp", tags = "系统APP版本管理")
public class SysAppController extends BaseController<SysApp,SysAppService> {

    private final  SysAppService sysAppService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysApp 系统APP版本
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_get')")
    public R getSysAppPage(Page<Integer> page, SysApp sysApp) {
        return super.query(sysApp, page);
    }

    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_get')")
    public R getSysAppList(SysApp sysApp) {
        return R.ok(sysAppService.lambdaQuery().list());
    }


    /**
     * 通过id查询系统APP版本
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增系统APP版本
     * @param sysApp 系统APP版本
     * @return R
     */
    @ApiOperation(value = "新增系统APP版本", notes = "新增系统APP版本")
    @PostMapping
    @SysLog("新增系统APP版本")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_add')")
    public R save(@Validated @RequestBody SysApp sysApp) {
        return super.update(sysApp, SecurityUtils.getUser().getId());
    }

    /**
     * 修改系统APP版本
     * @param sysApp 系统APP版本
     * @return R
     */
    @ApiOperation(value = "修改系统APP版本", notes = "修改系统APP版本")
    @PutMapping
    @SysLog("修改系统APP版本")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_edit')")
    public R updateById(@Validated @RequestBody SysApp sysApp) {
        return super.update(sysApp, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除系统APP版本
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统APP版本", notes = "通过id删除系统APP版本")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除系统APP版本")
    @PreAuthorize("@pms.hasPermission('upms_sysapp_del')")
    public R removeById(@PathVariable Integer id) {
        SysApp sysApp=super.service.queryById(id);
        if (sysApp ==null){
            return R.failed("ID错误");
        }
        return super.del(sysApp, SecurityUtils.getUser().getId());
    }

}
