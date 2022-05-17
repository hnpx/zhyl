package com.px.modulars.auth.handler;


import cn.hutool.core.thread.ThreadUtil;
import com.px.basic.alone.security.service.PigUser;
import com.px.modulars.upms.entity.SysPolicySecurity;
import com.px.modulars.upms.entity.SysUser;
import com.px.modulars.upms.service.SysPolicySecurityService;
import com.px.modulars.upms.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.*;
import org.springframework.stereotype.Component;

/**
 * @Description: 用户登录成功监听器事件
 * @ProjectName: spring-parent
 * @Version: 1.0
 */
@Component
public class AuthencationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    @Autowired
    private SysPolicySecurityService policySecurityService;
    @Autowired
    private SysUserService userService;

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            //提供的凭据是错误的，用户名或者密码错误
            System.out.println("---AuthenticationFailureBadCredentialsEvent---");
            ThreadUtil.execute(() -> {
                Object obj = event.getAuthentication().getPrincipal();
                if (obj == null) {
                    return;
                }
                SysUser user = this.userService.getUserByUsername(obj.toString());
                SysPolicySecurity policy = this.policySecurityService.readDef();
                boolean loginFlag = this.policySecurityService.isAllowLogin(policy, user, false);
                boolean codeFlag = this.policySecurityService.isCode(policy, user, false);
                //如果不允许访问，更新用户信息中的锁定时间
                if (!loginFlag || codeFlag) {
                    this.userService.updateById(user);
                }
            });
        } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            //验证通过，但是密码过期
            System.out.println("---AuthenticationFailureCredentialsExpiredEvent---");
        } else if (event instanceof AuthenticationFailureDisabledEvent) {
            //验证过了但是账户被禁用
            System.out.println("---AuthenticationFailureDisabledEvent---");
        } else if (event instanceof AuthenticationFailureExpiredEvent) {
            //验证通过了，但是账号已经过期
            System.out.println("---AuthenticationFailureExpiredEvent---");
        } else if (event instanceof AuthenticationFailureLockedEvent) {
            //账户被锁定
            System.out.println("---AuthenticationFailureLockedEvent---");
        } else if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            //配置错误，没有合适的AuthenticationProvider来处理登录验证
            System.out.println("---AuthenticationFailureProviderNotFoundEvent---");
        } else if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            //代理不受信任，用于Oauth、CAS这类三方验证的情形，多属于配置错误
            System.out.println("---AuthenticationFailureProxyUntrustedEvent---");
        } else if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            //其他任何在AuthenticationManager中内部发生的异常都会被封装成此类
            System.out.println("---AuthenticationFailureServiceExceptionEvent---");
        }
    }

}
