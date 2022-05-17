
package com.px.modulars.function.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.common.core.constant.Constants;
import com.pig4cloud.pig.common.core.util.R;
import com.px.modulars.function.entity.FunctionRoomClass;
import com.px.modulars.function.service.FunctionRoomClassService;
import com.px.basic.alone.core.base.BaseController;
import com.px.common.log.annotation.SysLog;
import com.px.basic.alone.security.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
 *
 * @author liupan
 * @date 2021-11-24 14:53:15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/functionroomclass")
@Api(value = "functionroomclass", tags = "功能室分类（长者之家，健康之家，暖夕阁，幸福之家）管理")
public class FunctionRoomClassController extends BaseController<FunctionRoomClass, FunctionRoomClassService> {

    private final FunctionRoomClassService functionRoomClassService;

    /**
     * 分页查询
     *
     * @param page              分页对象
     * @param functionRoomClass 功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('functionRoomClass_functionroomclass_get')")
    public R getFunctionRoomClassPage(Page page, FunctionRoomClass functionRoomClass) {
        Page functionRoomClassPage = functionRoomClassService.lambdaQuery()
                .eq(FunctionRoomClass::getEnable, Constants.ENABLE_TRUE)
                .like(StringUtils.isNotEmpty(functionRoomClass.getName()), FunctionRoomClass::getName, functionRoomClass.getName())
                .orderByDesc(FunctionRoomClass::getCreateTime)
                .page(page);
        return R.ok(functionRoomClassPage);
    }


    /**
     * 通过id查询功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('functionRoomClass_functionroomclass_get')")
    public R getById(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    /**
     * 新增功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     *
     * @param functionRoomClass 功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     * @return R
     */
    @ApiOperation(value = "新增功能室分类（长者之家，健康之家，暖夕阁，幸福之家）", notes = "新增功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @PostMapping
    @SysLog("新增功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @PreAuthorize("@pms.hasPermission('functionRoomClass_functionroomclass_add')")
    public R save( @RequestBody FunctionRoomClass functionRoomClass) {
        return super.update(functionRoomClass, SecurityUtils.getUser().getId());
    }

    /**
     * 修改功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     *
     * @param functionRoomClass 功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     * @return R
     */
    @ApiOperation(value = "修改功能室分类（长者之家，健康之家，暖夕阁，幸福之家）", notes = "修改功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @PutMapping
    @SysLog("修改功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @PreAuthorize("@pms.hasPermission('functionRoomClass_functionroomclass_edit')")
    public R updateById( @RequestBody FunctionRoomClass functionRoomClass) {
        return super.update(functionRoomClass, SecurityUtils.getUser().getId());
    }

    /**
     * 通过id删除功能室分类（长者之家，健康之家，暖夕阁，幸福之家）
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除功能室分类（长者之家，健康之家，暖夕阁，幸福之家）", notes = "通过id删除功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @DeleteMapping("/{id}")
    @SysLog("通过id删除功能室分类（长者之家，健康之家，暖夕阁，幸福之家）")
    @PreAuthorize("@pms.hasPermission('functionRoomClass_functionroomclass_del')")
    public R removeById(@PathVariable Integer id) {
        FunctionRoomClass functionRoomClass = super.service.queryById(id);
        if (functionRoomClass == null) {
            return R.failed("ID错误");
        }
        return super.del(functionRoomClass, SecurityUtils.getUser().getId());
    }

    @ApiOperation("功能室分类列表")
    @GetMapping("/list")
    public R getList() {

        List<FunctionRoomClass> functionRoomClassList = functionRoomClassService.lambdaQuery().eq(FunctionRoomClass::getEnable, Constants.ENABLE_TRUE)
                .orderByAsc(FunctionRoomClass::getCreateTime).list();
        return R.ok(functionRoomClassList);

    }

}
