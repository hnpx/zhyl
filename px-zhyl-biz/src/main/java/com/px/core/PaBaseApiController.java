package com.px.core;

import com.pig4cloud.pig.common.core.exception.LoginException;
import com.px.basic.alone.core.base.AbstractController;
import com.px.basic.alone.security.annotation.Inner;
import com.px.basic.alone.security.jwt.JwtTokenUtil;
import com.px.basic.alone.security.jwt.payload.JwtPayLoad;
import com.px.core.util.RedisHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 */
@RequestMapping({"/api"})
@Inner(false)
public class PaBaseApiController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(PaBaseApiController.class);
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected RedisHelper redisHelper;
    @Value("${api.token-key}")
    private String apiTokenKey;
    @Value("${api.jwt-secret}")
    private String jwtSecret;
    @Value("${api.jwt-expire-seconds}")
    private Long expireSeconds;

    public PaBaseApiController() {
    }

    protected Long getCurrentId() {
        String token = this.request.getHeader(this.apiTokenKey);
        try{
        if (token == null) {

            throw  new LoginException();
        } else {
            Object idObj = this.redisHelper.get(token);
            if (idObj == null) {
                throw new LoginException();
            } else {
                log.info("==============================================" + idObj);
                
                return idObj == null ? null : Long.parseLong(idObj.toString());
            }
        }}catch(Exception e){
            e.printStackTrace();

            throw new LoginException();
        }
    }

    protected String createToken(String id, String username, int saveTime) {
        String token = this.createToken(id, username);
        if (saveTime > 0) {
            this.redisHelper.set(this.apiTokenKey + "_" + token, id, saveTime);
        }

        return token;
    }

    protected String createToken(String id, String username) {
        JwtPayLoad payLoad = new JwtPayLoad(id, username, this.apiTokenKey);
        String token = JwtTokenUtil.generateToken(payLoad);
        return token;
    }

    protected void clearToken() {
        String token = this.request.getHeader(this.apiTokenKey);
        if (token != null) {
            this.redisHelper.del(this.apiTokenKey + "_" + token);
        }
    }


    protected void putTokenToHeader(String token) {
        this.response.setHeader(this.apiTokenKey, token);
    }
}
