
package com.px.modulars.replace.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.modulars.replace.entity.ReplaceBuy;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.modulars.replace.service.ReplaceBuyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 代买代办服务类别
 *
 * @author px code generator
 * @date 2021-11-26 16:07:16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/replaceBuy/replacebuy")
@Api(value = "replacebuy", tags = "代买代办服务类别管理")
public class ReplaceBuyController extends BaseController<ReplaceBuy, ReplaceBuyService> {

    private final ReplaceBuyService replaceBuyService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param replaceBuy 代买代办服务类别
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('replaceBuy_replacebuy_get')")
    public R getReplaceBuyPage(Page page, ReplaceBuy replaceBuy) {
        Page<ReplaceBuy> replaceBuyPage = replaceBuyService.lambdaQuery().eq(ReplaceBuy::getEnable, Constants.ENABLE_TRUE)
                .like(StringUtils.isNotEmpty(replaceBuy.getName()), ReplaceBuy::getName, replaceBuy.getName())
                .orderByDesc(ReplaceBuy::getCreateTime)
                .page(page);
        return R.ok(replaceBuyPage);
    }


    /**
     * 通过id查询代买代办服务类别
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('replaceBuy_replacebuy_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增代买代办服务类别
     *
     * @param replaceBuy 代买代办服务类别
     * @return R
     */
    @ApiOperation(value = "新增代买代办服务类别", notes = "新增代买代办服务类别")
    @PostMapping
    @SysLog("新增代买代办服务类别")
    @PreAuthorize("@pms.hasPermission('replaceBuy_replacebuy_add')")
    public R save( @RequestBody ReplaceBuy replaceBuy) {
        return super.update(replaceBuy, SecurityUtils.getUser().getId());
    }

    /**
     * 修改代买代办服务类别
     *
     * @param replaceBuy 代买代办服务类别
     * @return R
     */
    @ApiOperation(value = "修改代买代办服务类别", notes = "修改代买代办服务类别")
    @PutMapping
    @SysLog("修改代买代办服务类别")
    @PreAuthorize("@pms.hasPermission('replaceBuy_replacebuy_edit')")
    public R updateById( @RequestBody ReplaceBuy replaceBuy) {
        return super.update(replaceBuy, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除代买代办服务类别
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除代买代办服务类别", notes = "通过id删除代买代办服务类别")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除代买代办服务类别")
    @PreAuthorize("@pms.hasPermission('replaceBuy_replacebuy_del')")
    public R removeById(@PathVariable Integer id) {
        ReplaceBuy replaceBuy = super.service.queryById(id);
        if (replaceBuy == null) {
            return R.failed("ID错误");
        }
        return super.del(replaceBuy, SecurityUtils.getUser().getId());
    }

}
