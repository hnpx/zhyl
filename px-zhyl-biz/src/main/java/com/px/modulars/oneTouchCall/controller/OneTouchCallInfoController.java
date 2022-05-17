
package com.px.modulars.oneTouchCall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.util.R;
import com.px.modulars.oneTouchCall.entity.OneTouchCallInfo;
import com.px.modulars.oneTouchCall.service.OneTouchCallInfoService;
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
 * 一键呼叫号码
 *
 * @author XX
 * @date 2021-12-10 15:14:06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oneTouchCall/onetouchcallinfo")
@Api(value = "onetouchcallinfo", tags = "一键呼叫号码管理")
public class OneTouchCallInfoController extends BaseController<OneTouchCallInfo,OneTouchCallInfoService> {

    private final  OneTouchCallInfoService oneTouchCallInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param oneTouchCallInfo 一键呼叫号码
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('oneTouchCall_onetouchcallinfo_get')")
    public R getOneTouchCallInfoPage(Page<Integer> page, OneTouchCallInfo oneTouchCallInfo) {
        return super.query(oneTouchCallInfo, page);
    }


    /**
     * 通过id查询一键呼叫号码
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('oneTouchCall_onetouchcallinfo_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增一键呼叫号码
     * @param oneTouchCallInfo 一键呼叫号码
     * @return R
     */
    @ApiOperation(value = "新增一键呼叫号码", notes = "新增一键呼叫号码")
    @PostMapping
    @SysLog("新增一键呼叫号码")
    @PreAuthorize("@pms.hasPermission('oneTouchCall_onetouchcallinfo_add')")
    public R save(@Validated @RequestBody OneTouchCallInfo oneTouchCallInfo) {
        return super.update(oneTouchCallInfo, SecurityUtils.getUser().getId());
    }

    /**
     * 修改一键呼叫号码
     * @param oneTouchCallInfo 一键呼叫号码
     * @return R
     */
    @ApiOperation(value = "修改一键呼叫号码", notes = "修改一键呼叫号码")
    @PutMapping
    @SysLog("修改一键呼叫号码")
    @PreAuthorize("@pms.hasPermission('oneTouchCall_onetouchcallinfo_edit')")
    public R updateById(@Validated @RequestBody OneTouchCallInfo oneTouchCallInfo) {
        return super.update(oneTouchCallInfo, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除一键呼叫号码
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除一键呼叫号码", notes = "通过id删除一键呼叫号码")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除一键呼叫号码")
    @PreAuthorize("@pms.hasPermission('oneTouchCall_onetouchcallinfo_del')")
    public R removeById(@PathVariable Integer id) {
        OneTouchCallInfo oneTouchCallInfo=super.service.queryById(id);
        if (oneTouchCallInfo ==null){
            return R.failed("ID错误");
        }
        return super.del(oneTouchCallInfo, SecurityUtils.getUser().getId());
    }

}
