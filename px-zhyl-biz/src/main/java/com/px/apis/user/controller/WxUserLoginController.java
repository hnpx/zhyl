package com.px.apis.user.controller;

import com.pig4cloud.pig.common.core.util.R;
import com.px.basic.alone.security.dto.AbsAppUser;
import com.px.wx.mini.core.WxMiniUserBaseController;
import com.px.wx.vo.param.AbsWxLoginUserInfoParam;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouz
 * @Date 2021/5/22 11:18
 * @Description
 */
@RestController
@RequestMapping("/api/wx/user")
@RequiredArgsConstructor
@Api(value = "user", tags = "小程序端登录接口管理")
public class WxUserLoginController extends WxMiniUserBaseController {
    @Override
    protected AbsAppUser autoUserLogin(String s) {
        return null;
    }

    @Override
    protected AbsAppUser userLogin(AbsWxLoginUserInfoParam absWxLoginUserInfoParam) {
        return null;
    }

    @Override
    protected String getAppid() {
        return null;
    }


    @PostMapping("/login")
    public R login(@RequestBody AbsWxLoginUserInfoParam param) {
        AbsAppUser user = super.loginHandler(param);
        return super.setSuccessModelMap(user);
    }

    @PostMapping("/autoLogin")
    public R autoLogin(@RequestBody AbsWxLoginUserInfoParam param) {
        AbsAppUser user = super.autoLoginHandler(param);
        return super.setSuccessModelMap(user);
    }

}
