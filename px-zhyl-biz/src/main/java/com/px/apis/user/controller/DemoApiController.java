package com.px.apis.user.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.pig4cloud.pig.common.core.util.R;
import com.px.apis.user.vo.DemoEntity;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.basic.alone.security.annotation.Inner;
import com.px.basic.alone.validation.aop.ParamReplace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouz
 * @Date 2021/5/24 14:51
 * @Description
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo/demoinfo")
@Api(value = "demoinfo", tags = "测试API服务")
public class DemoApiController extends BaseApiController {
    @GetMapping
    @ApiOperation("测试")
    public R demo() {
        return super.setSuccessModelMap();
    }

    @PostMapping
    @ParamReplace("*")
    public R hello(@RequestBody DemoEntity demo) {
        return R.ok();
    }

}
