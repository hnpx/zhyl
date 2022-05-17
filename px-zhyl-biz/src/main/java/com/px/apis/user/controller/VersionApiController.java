package com.px.apis.user.controller;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.core.base.BaseApiController;
import com.px.modulars.upms.entity.SysApp;
import com.px.modulars.upms.service.SysAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.oschina.j2cache.CacheChannel;
import org.springframework.web.bind.annotation.*;
import sun.misc.Cache;

/**
 * @author zhouz
 * @Date 2021/5/24 14:51
 * @Description
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/version")
@Api(value = "appversion", tags = "APP版本号")
public class VersionApiController extends BaseApiController {

    private final SysAppService appService;

    private final CacheChannel cacheChannel;

    private static final String region = "SYSTEM_APPLICATION_VERSION";

    @GetMapping
    @ApiOperation("获取版本号")
    public R getVersion(String key) {
        Object obj = cacheChannel.get(region, key);
        SysApp app;
        if (obj != null) {
            app = (SysApp) obj;
        } else {
            app = appService.lambdaQuery().eq(StrUtil.isNotEmpty(key), SysApp::getAppkey, key).one();
            cacheChannel.set(region, key, app);
        }
        return super.setSuccessModelMap(app);
    }


}
