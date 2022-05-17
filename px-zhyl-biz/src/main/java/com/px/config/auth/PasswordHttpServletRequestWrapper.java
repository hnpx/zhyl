package com.px.config.auth;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.px.config.db.RedisConfig;
import com.px.core.util.RedisHelper;
import io.lettuce.core.RedisClient;
import javafx.scene.shape.Circle;
import lombok.RequiredArgsConstructor;
import org.codehaus.plexus.component.annotations.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import com.px.basic.alone.security.service.PigUserDetailsServiceImpl;
import com.px.core.util.RedisHelper;
import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 处理密码的Request
 *
 * @author zhouz
 */

public class PasswordHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final String PASSWORD = "password";
    private static final String KEY_ALGORITHM = "AES";
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    private RedisHelper redisHelper;
    private PasswordEncoder encoder;
    @Resource(
            name = "ARedisHelperGetName"
    )


    private HashMap<String, String[]> newParam = null;
    private String encodeKey;

    public PasswordHttpServletRequestWrapper(HttpServletRequest request, String encodeKey, PasswordEncoder encoder) {
        super(request);
        this.encodeKey = encodeKey;
        this.encoder = encoder;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Enumeration<String> enumeration = super.getParameterNames();
        ArrayList<String> list = Collections.list(enumeration);
        //当有token字段时动态的添加uid字段
//        if (list.contains(PASSWORD)){
//            return Collections.enumeration(list);
//        }else {
        return super.getParameterNames();
//        }
    }

    @SneakyThrows
    @Override
    public String getParameter(String name) {

        if (PASSWORD.equals(name)) {
            String[] passwords = super.getParameterValues(name);
            //TODO 默认在第0个
            String password = passwords[0];
            password = decryptAES(password, encodeKey);
            password = password.trim();
            //TODO 识别到从双报到系统中进入，返回username作为密码,前几位是用户名，后四位是密钥
            if (password.length() >= 17) {
                Base64 base64 = new Base64();
                String a = new String(base64.decode(password), "UTF-8");
                boolean equals = a.startsWith("PinXunKeJi");
                boolean equals2 = a.endsWith("123!@#.");
                if (equals && equals2) {
                    return a;
                }
            }
            //String pd = encoder.encode(password);

            return password;
        }
        return super.getParameter(name);
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        HashMap<String, String[]> newMap = new HashMap<>();
        newMap.putAll(super.getParameterMap());
        newMap.put(PASSWORD, this.getParameterValues(PASSWORD));
        return Collections.unmodifiableMap(newMap);
    }


    private static String decryptAES(String data, String pass) {
        AES aes = new AES(Mode.CBC, Padding.NoPadding, new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM),
                new IvParameterSpec(pass.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(data.getBytes(StandardCharsets.UTF_8)));
        return new String(result, StandardCharsets.UTF_8);
    }


    @Override
    public String[] getParameterValues(String name) {
        if (PASSWORD.equals(name)) {
            return new String[]{this.getParameter(name)};
        }
        return super.getParameterValues(name);
    }
}
