package com.px.modulars.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.px.basic.alone.core.base.BaseService;
import com.px.modulars.upms.entity.SysPolicySecurity;
import com.px.modulars.upms.entity.SysUser;
import com.px.modulars.upms.vo.VerificationCodeVO;
import org.omg.CORBA.ServerRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全配置
 *
 * @author 吕郭飞
 * @date 2021-07-06 17:48:11
 */
public interface SysPolicySecurityService extends IService<SysPolicySecurity>, BaseService<SysPolicySecurity> {
    public Integer defId = 1;

    public SysPolicySecurity readDef();

    /**
     * 检查用户下次登录是否需要输入验证码，如果需要则返回标记
     *
     * @param policy    规则
     * @param user      用户信息
     * @param loginFlag 是否登录成功
     * @return
     */
    public boolean isCode(SysPolicySecurity policy, SysUser user, boolean loginFlag);

    /**
     * 创建验证码
     *
     * @param policy 规则
     * @return
     */
    public VerificationCodeVO createVerificationCode(SysPolicySecurity policy, HttpServletRequest request);

    /**
     * 检查Code是否正确
     *
     * @param policy   规则
     * @param username 用户名
     * @param token    验证码标识
     * @param code     验证码
     * @return
     */
    public boolean checkCode(SysPolicySecurity policy, String username, String token, String code);

    /**
     * 是否允许登录
     *
     * @param policy    规则
     * @param user      登录用户
     * @param loginFlag 登录标志
     * @return
     */
    public boolean isAllowLogin(SysPolicySecurity policy, SysUser user, boolean loginFlag);
}
