package com.px.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.support.http.HttpCode;
import com.px.basic.alone.security.util.SecurityUtils;
import com.px.config.auth.PasswordHttpServletRequestWrapper;
import com.px.core.util.RedisHelper;
import com.px.modulars.upms.entity.SysPolicySecurity;
import com.px.modulars.upms.entity.SysUser;
import com.px.modulars.upms.service.SysPolicySecurityService;
import com.px.modulars.upms.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 密码解密过滤器
 *
 * @author zhouz
 */
@WebFilter(urlPatterns = "/*", filterName = "PasswordDecoderFilter")
@Slf4j
public class PasswordDecoderFilter extends HttpFilter {


    @Value("${security.encode.key:1234567812345678}")
    private String encodeKey;
    @Autowired
    private SysPolicySecurityService policySecurityService;

//    @Autowired
//    private PasswordEncoder encoding;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String nowUrl = request.getRequestURI();
        // 不是登录请求，直接向下执行
        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        request = new PasswordHttpServletRequestWrapper(request, this.encodeKey, new BCryptPasswordEncoder());
        //获取Code
        String code = request.getParameter("code");
        String token = request.getParameter("token");
        String username = request.getParameter("username");
        SysPolicySecurity polic = this.policySecurityService.readDef();
        boolean codeFlag = this.policySecurityService.checkCode(polic, username, token, code);
        //CODE错误
        if (!codeFlag) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpCode.BAD_REQUEST.value());
            result.put("msg", "验证码错误");
            servletResponse.getWriter().print(JSONUtil.toJsonStr(result));
            return;
        }
        chain.doFilter(request, servletResponse);
        return;
    }
}
